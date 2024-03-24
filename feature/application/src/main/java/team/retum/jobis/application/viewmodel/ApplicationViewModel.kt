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
import team.retum.common.exception.NotFoundException
import team.retum.common.exception.UnAuthorizedException
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

    private val attachments: MutableList<Pair<String, AttachmentType>> = mutableListOf()

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

    private fun setButtonEnabled() = setState {
        state.value.copy(buttonEnabled = files.isNotEmpty())
    }

    internal fun addUrl() {
        urls.add("")
    }

    internal fun removeUrl(index: Int) {
        urls.removeAt(index)
    }

    internal fun onUrlChange(
        index: Int,
        url: String,
    ) {
        urls[index] = url
    }

    private suspend fun applyCompany() {
        applyCompanyUseCase(
            recruitmentId = state.value.recruitmentId,
            attachments = attachments,
        ).onSuccess {
            postSideEffect(ApplicationSideEffect.SuccessApply)
        }.onApplyCompanyFailure()
    }

    private suspend fun reApplyCompany() {
        reApplyCompanyUseCase(
            applicationId = state.value.applicationId,
            attachments = attachments,
        ).onSuccess {
            postSideEffect(ApplicationSideEffect.SuccessReApply)
        }.onApplyCompanyFailure()
    }

    private fun Result<Unit>.onApplyCompanyFailure() {
        onFailure {
            when (it) {
                is UnAuthorizedException -> {
                    postSideEffect(ApplicationSideEffect.UnexpectedGrade)
                }

                is NotFoundException -> {
                    postSideEffect(ApplicationSideEffect.NotFoundRecruitment)
                }

                is ConflictException -> {
                    postSideEffect(ApplicationSideEffect.ConflictApply)
                }
            }
        }
    }

    internal fun createPresignedUrl() {
        setState { state.value.copy(buttonEnabled = false) }
        viewModelScope.launch(Dispatchers.IO) {
            createPresignedUseCase(files = files.toFileNames()).onSuccess {
                addPresignedUrlOrUrlToAttachments(urls = it.urls.toFileTypeAttachments())
                addPresignedUrlOrUrlToAttachments(urls = urls.toUrlTypeAttachments())
                if (state.value.isReApply) {
                    reApplyCompany()
                } else {
                    applyCompany()
                }
                uploadFile(presignedUrls = it.urls.toPresignedUrls())
            }.onFailure {
                when (it) {
                    is BadRequestException -> {
                        postSideEffect(ApplicationSideEffect.InvalidFileExtension)
                    }
                }
            }
        }
    }

    private suspend fun uploadFile(presignedUrls: List<String>) {
        presignedUrls.forEachIndexed { index, presignedUrl ->
            uploadFileUseCase(
                presignedUrl = presignedUrl,
                file = files[index],
            )
        }
    }

    private fun List<File>.toFileNames() = map { it.name }

    private fun List<PresignedUrlEntity.UrlEntity>.toFileTypeAttachments() =
        map { it.filePath to AttachmentType.FILE }

    private fun List<PresignedUrlEntity.UrlEntity>.toPresignedUrls() =
        map { it.preSignedUrl }

    private fun List<String>.toUrlTypeAttachments() =
        map { it to AttachmentType.URL }

    private fun addPresignedUrlOrUrlToAttachments(urls: List<Pair<String, AttachmentType>>) {
        attachments.addAll(urls)
    }
}

internal data class ApplicationState(
    val recruitmentId: Long,
    val buttonEnabled: Boolean,
    val isReApply: Boolean,
    val applicationId: Long,
) {
    companion object {
        fun getInitialState() = ApplicationState(
            recruitmentId = 0,
            buttonEnabled = false,
            isReApply = false,
            applicationId = 0L,
        )
    }
}

internal sealed interface ApplicationSideEffect {
    data object ExceedFileCount : ApplicationSideEffect
    data object InvalidFileExtension : ApplicationSideEffect
    data object SuccessApply : ApplicationSideEffect
    data object SuccessReApply : ApplicationSideEffect
    data object ConflictApply : ApplicationSideEffect
    data object NotFoundRecruitment : ApplicationSideEffect
    data object UnexpectedGrade : ApplicationSideEffect
}
