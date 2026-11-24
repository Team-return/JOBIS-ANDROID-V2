package team.retum.bug.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.DevelopmentArea
import team.retum.common.utils.FileUtil
import team.retum.usecase.entity.file.PresignedUrlEntity
import team.retum.usecase.usecase.bug.ReportBugUseCase
import team.retum.usecase.usecase.file.CreatePresignedUrlUseCase
import team.retum.usecase.usecase.file.UploadFileUseCase
import java.io.File
import javax.inject.Inject

private const val MAX_SCREENSHOT_COUNT = 5

@HiltViewModel
internal class ReportBugViewModel @Inject constructor(
    private val reportBugUseCase: ReportBugUseCase,
    private val createPresignedUrlUseCase: CreatePresignedUrlUseCase,
    private val uploadFileUseCase: UploadFileUseCase,
) : BaseViewModel<ReportBugState, ReportBugSideEffect>(ReportBugState.getInitialState()) {

    private val screenshots: SnapshotStateList<Uri> = mutableStateListOf()

    internal fun addUris(uris: List<Uri>) {
        if (state.value.isSubmitting) return
        if (screenshots.size + uris.size > MAX_SCREENSHOT_COUNT) {
            postSideEffect(ReportBugSideEffect.MaxScreenshotCount)
        } else {
            screenshots.addAll(uris)
        }
    }

    internal fun getUris(): List<Uri> = screenshots

    internal fun removeScreenshot(index: Int) {
        if (state.value.isSubmitting) return
        screenshots.removeAt(index)
    }

    internal fun setTitle(title: String) = setState {
        with(state.value) {
            copy(
                title = title,
                buttonEnabled = title.isNotBlank() && content.isNotBlank() && !isSubmitting,
            )
        }
    }

    internal fun setContent(content: String) = setState {
        with(state.value) {
            copy(
                content = content,
                buttonEnabled = title.isNotBlank() && content.isNotBlank() && !isSubmitting,
            )
        }
    }

    internal fun setDevelopmentArea(developmentArea: DevelopmentArea) = setState {
        state.value.copy(developmentArea = developmentArea)
    }

    private fun setSubmitting(isSubmitting: Boolean) = setState {
        with(state.value) {
            copy(
                buttonEnabled = title.isNotBlank() && content.isNotBlank() && !isSubmitting,
                isSubmitting = isSubmitting,
            )
        }
    }

    internal fun onNextClick(context: Context) {
        if (state.value.isSubmitting) return
        val submissionState = state.value
        val submissionScreenshots = screenshots.toList()
        val applicationContext = context.applicationContext
        setSubmitting(isSubmitting = true)
        viewModelScope.launch(Dispatchers.IO) {
            val submissionFiles = runCatching {
                submissionScreenshots.map {
                    FileUtil.toFile(
                        context = applicationContext,
                        uri = it,
                    )
                }
            }.getOrElse {
                onUploadFailure()
                return@launch
            }
            if (submissionFiles.isEmpty()) {
                reportBug(
                    submissionState = submissionState,
                    attachmentUrls = emptyList(),
                )
            } else {
                createPresignedUrlUseCase(files = submissionFiles.map { it.name })
                    .onSuccess {
                        if (uploadFiles(urls = it.urls, files = submissionFiles)) {
                            reportBug(
                                submissionState = submissionState,
                                attachmentUrls = it.urls.map { url -> url.filePath },
                            )
                        } else {
                            onUploadFailure()
                        }
                    }.onFailure {
                        onUploadFailure()
                    }
            }
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

    private suspend fun reportBug(
        submissionState: ReportBugState,
        attachmentUrls: List<String>,
    ) {
        reportBugUseCase(
            title = submissionState.title,
            content = submissionState.content,
            developmentArea = submissionState.developmentArea,
            attachmentUrls = attachmentUrls,
        ).onSuccess {
            postSideEffect(ReportBugSideEffect.Success)
        }.onFailure {
            setSubmitting(isSubmitting = false)
            postSideEffect(ReportBugSideEffect.SubmitFailure)
        }
    }

    private fun onUploadFailure() {
        setSubmitting(isSubmitting = false)
        postSideEffect(ReportBugSideEffect.UploadFailure)
    }
}

internal data class ReportBugState(
    val title: String,
    val content: String,
    val developmentArea: DevelopmentArea,
    val buttonEnabled: Boolean,
    val isSubmitting: Boolean,
) {
    companion object {
        fun getInitialState() = ReportBugState(
            title = "",
            content = "",
            developmentArea = DevelopmentArea.ANDROID,
            buttonEnabled = false,
            isSubmitting = false,
        )
    }
}

internal sealed interface ReportBugSideEffect {
    data object MaxScreenshotCount : ReportBugSideEffect
    data object Success : ReportBugSideEffect
    data object UploadFailure : ReportBugSideEffect
    data object SubmitFailure : ReportBugSideEffect
}
