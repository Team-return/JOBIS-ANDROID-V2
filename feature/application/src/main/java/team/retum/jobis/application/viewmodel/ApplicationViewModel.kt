package team.retum.jobis.application.viewmodel

import android.content.ClipData
import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.AttachmentType
import team.retum.common.exception.BadRequestException
import team.retum.common.exception.ConflictException
import team.retum.common.exception.ForbiddenException
import team.retum.common.exception.NotFoundException
import team.retum.common.utils.FileUtil
import team.retum.usecase.entity.file.PresignedUrlEntity
import team.retum.usecase.usecase.application.ApplyCompanyUseCase
import team.retum.usecase.usecase.application.ReApplyCompanyUseCase
import team.retum.usecase.usecase.file.CreatePresignedUrlUseCase
import team.retum.usecase.usecase.file.UploadFileUseCase
import java.io.File
import javax.inject.Inject

internal const val MAX_ATTACHMENT_COUNT = 3

@HiltViewModel
internal class ApplicationViewModel @Inject constructor(
    private val applyCompanyUseCase: ApplyCompanyUseCase,
    private val reApplyCompanyUseCase: ReApplyCompanyUseCase,
    private val createPresignedUseCase: CreatePresignedUrlUseCase,
    private val uploadFileUseCase: UploadFileUseCase,
) : BaseViewModel<ApplicationState, ApplicationSideEffect>(ApplicationState.getInitialState()) {

    private val files: SnapshotStateList<File> = mutableStateListOf()

    private val urls: SnapshotStateList<String> = mutableStateListOf()

    internal fun getFiles() = files

    internal fun getUrls() = urls

    internal fun setRecruitmentId(recruitmentId: Long) = setState {
        state.value.copy(recruitmentId = recruitmentId)
    }

    internal fun setApplicationId(applicationId: Long) = setState {
        state.value.copy(applicationId = applicationId)
    }

    internal fun setIsReApply(isReApply: Boolean) = setState {
        state.value.copy(isReApply = isReApply)
    }

    internal fun addFile(
        activityResult: ActivityResult,
        context: Context,
    ) {
        val result = activityResult.data
        result?.run {
            clipData?.run {
                addUriToFiles(
                    uris = this,
                    context = context,
                )
            }
            data?.run {
                if (files.size >= MAX_ATTACHMENT_COUNT) {
                    postSideEffect(ApplicationSideEffect.ExceedFileCount)
                } else {
                    addFile(
                        uri = this,
                        context = context,
                    )
                }
            }
        }
        setButtonEnabled()
    }

    private fun addUriToFiles(
        uris: ClipData,
        context: Context,
    ) {
        val count = uris.itemCount
        if (files.size + count > MAX_ATTACHMENT_COUNT) {
            postSideEffect(ApplicationSideEffect.ExceedFileCount)
        } else {
            repeat(uris.itemCount) {
                val uri = uris.getItemAt(it).uri
                addFile(
                    uri = uri,
                    context = context,
                )
            }
        }
    }

    private fun addFile(
        uri: Uri,
        context: Context,
    ) {
        files.add(
            FileUtil.toFile(
                context = context,
                uri = uri,
            ),
        )
    }

    internal fun removeFile(index: Int) {
        files.removeAt(index)
        setButtonEnabled()
    }

    private fun setButtonEnabled(isSubmitting: Boolean = state.value.isSubmitting) = setState {
        state.value.copy(
            buttonEnabled = (files.isNotEmpty() || urls.any(String::isNotBlank)) && !isSubmitting,
            isSubmitting = isSubmitting,
        )
    }

    internal fun addUrl() {
        urls.add("")
    }

    internal fun removeUrl(index: Int) {
        urls.removeAt(index)
        setButtonEnabled()
    }

    internal fun onUrlChange(
        index: Int,
        url: String,
    ) {
        urls[index] = url
        setButtonEnabled()
    }

    private suspend fun applyCompany(attachments: List<Pair<String, AttachmentType>>) {
        applyCompanyUseCase(
            recruitmentId = state.value.recruitmentId,
            attachments = attachments,
        ).onSuccess {
            postSideEffect(ApplicationSideEffect.SuccessApply)
        }.onApplyCompanyFailure(isReApply = false)
    }

    private suspend fun reApplyCompany(attachments: List<Pair<String, AttachmentType>>) {
        reApplyCompanyUseCase(
            applicationId = state.value.applicationId,
            attachments = attachments,
        ).onSuccess {
            postSideEffect(ApplicationSideEffect.SuccessReApply)
        }.onApplyCompanyFailure(isReApply = true)
    }

    private fun Result<Unit>.onApplyCompanyFailure(isReApply: Boolean) {
        onFailure {
            if (it !is KotlinNullPointerException) {
                setButtonEnabled(isSubmitting = false)
            }
            when (it) {
                is KotlinNullPointerException -> {
                    postSideEffect(
                        sideEffect = when (isReApply) {
                            true -> ApplicationSideEffect.SuccessReApply
                            else -> ApplicationSideEffect.SuccessApply
                        },
                    )
                }

                is ForbiddenException -> {
                    postSideEffect(ApplicationSideEffect.UnexpectedGrade)
                }

                is NotFoundException -> {
                    postSideEffect(ApplicationSideEffect.NotFoundRecruitment)
                }

                is ConflictException -> {
                    postSideEffect(ApplicationSideEffect.ConflictApply)
                }

                else -> {
                    postSideEffect(ApplicationSideEffect.ApplyFailure(isReApply = isReApply))
                }
            }
        }
    }

    internal fun createPresignedUrl() {
        if (state.value.isSubmitting) return
        setButtonEnabled(isSubmitting = true)
        val submissionFiles = files.toList()
        val submissionUrls = urls.filter(String::isNotBlank)
        viewModelScope.launch(Dispatchers.IO) {
            if (submissionFiles.isEmpty()) {
                submitApplication(attachments = submissionUrls.toUrlTypeAttachments())
            } else {
                createPresignedUseCase(files = submissionFiles.toFileNames()).onSuccess {
                    if (uploadFiles(urls = it.urls, files = submissionFiles)) {
                        submitApplication(
                            attachments = it.urls.toFileTypeAttachments() + submissionUrls.toUrlTypeAttachments(),
                        )
                    } else {
                        setButtonEnabled(isSubmitting = false)
                        postSideEffect(ApplicationSideEffect.AttachmentUploadFailure)
                    }
                }.onFailure {
                    setButtonEnabled(isSubmitting = false)
                    when (it) {
                        is BadRequestException -> {
                            postSideEffect(ApplicationSideEffect.InvalidFileExtension)
                        }

                        else -> {
                            postSideEffect(ApplicationSideEffect.ApplyFailure(isReApply = state.value.isReApply))
                        }
                    }
                }
            }
        }
    }

    private suspend fun submitApplication(attachments: List<Pair<String, AttachmentType>>) {
        if (state.value.isReApply) {
            reApplyCompany(attachments = attachments)
        } else {
            applyCompany(attachments = attachments)
        }
    }

    private suspend fun uploadFiles(
        urls: List<PresignedUrlEntity.UrlEntity>,
        files: List<File>,
    ): Boolean =
        urls.size == files.size &&
            urls.zip(files).all { (url, file) ->
                uploadFileUseCase(
                    presignedUrl = url.preSignedUrl,
                    file = file,
                ).isSuccess
            }

    private fun List<File>.toFileNames() = map { it.name }

    private fun List<PresignedUrlEntity.UrlEntity>.toFileTypeAttachments() =
        map { it.filePath to AttachmentType.FILE }

    private fun List<String>.toUrlTypeAttachments() =
        map { it to AttachmentType.URL }
}

internal data class ApplicationState(
    val recruitmentId: Long,
    val buttonEnabled: Boolean,
    val isSubmitting: Boolean,
    val isReApply: Boolean,
    val applicationId: Long,
) {
    companion object {
        fun getInitialState() = ApplicationState(
            recruitmentId = 0,
            buttonEnabled = false,
            isSubmitting = false,
            isReApply = false,
            applicationId = 0L,
        )
    }
}

internal sealed interface ApplicationSideEffect {
    data object ExceedFileCount : ApplicationSideEffect
    data object InvalidFileExtension : ApplicationSideEffect
    data object AttachmentUploadFailure : ApplicationSideEffect
    data object SuccessApply : ApplicationSideEffect
    data object SuccessReApply : ApplicationSideEffect
    data object ConflictApply : ApplicationSideEffect
    data object NotFoundRecruitment : ApplicationSideEffect
    data object UnexpectedGrade : ApplicationSideEffect
    data class ApplyFailure(val isReApply: Boolean) : ApplicationSideEffect
}
