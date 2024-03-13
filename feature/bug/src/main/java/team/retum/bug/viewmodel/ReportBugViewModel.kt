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
    private val files: MutableList<File> = mutableListOf()
    private val attachmentUrls: MutableList<String> = mutableListOf()

    internal fun addUris(uris: List<Uri>) {
        if (screenshots.size + uris.size > MAX_SCREENSHOT_COUNT) {
            postSideEffect(ReportBugSideEffect.MaxScreenshotCount)
        } else {
            screenshots.addAll(uris)
        }
    }

    internal fun getUris(): List<Uri> = screenshots

    internal fun removeScreenshot(index: Int) {
        screenshots.removeAt(index)
    }

    internal fun setTitle(title: String) = setState {
        setButtonEnabled(title = title)
        state.value.copy(title = title)
    }

    internal fun setContent(content: String) = setState {
        setButtonEnabled(content = content)
        state.value.copy(content = content)
    }

    internal fun setDevelopmentArea(developmentArea: DevelopmentArea) = setState {
        state.value.copy(developmentArea = developmentArea)
    }

    private fun setButtonEnabled(
        title: String = state.value.title,
        content: String = state.value.content,
    ) = setState {
        state.value.copy(buttonEnabled = title.isNotBlank() && content.isNotBlank())
    }

    internal fun onNextClick(context: Context) {
        createPresignedUrl(context)
    }

    private fun createPresignedUrl(context: Context) {
        files.addAll(
            screenshots.map {
                FileUtil.toFile(
                    context = context,
                    uri = it,
                )
            },
        )
        viewModelScope.launch(Dispatchers.IO) {
            createPresignedUrlUseCase(
                files = files.map { it.name },
            ).onSuccess {
                it.urls.forEachIndexed { index, urlEntity ->
                    attachmentUrls.add(urlEntity.filePath)
                    uploadFile(
                        presignedUrl = urlEntity.preSignedUrl,
                        file = files[index],
                    )
                }
                reportBug()
            }
        }
    }

    private fun uploadFile(
        presignedUrl: String,
        file: File,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            uploadFileUseCase(
                presignedUrl = presignedUrl,
                file = file,
            )
        }
    }

    private fun reportBug() {
        with(state.value) {
            viewModelScope.launch(Dispatchers.IO) {
                reportBugUseCase(
                    title = title,
                    content = content,
                    developmentArea = developmentArea,
                    attachmentUrls = attachmentUrls,
                ).onSuccess {
                    postSideEffect(ReportBugSideEffect.Success)
                }
            }
        }
    }
}

internal data class ReportBugState(
    val title: String,
    val content: String,
    val developmentArea: DevelopmentArea,
    val buttonEnabled: Boolean,
) {
    companion object {
        fun getInitialState() = ReportBugState(
            title = "",
            content = "",
            developmentArea = DevelopmentArea.ANDROID,
            buttonEnabled = false,
        )
    }
}

internal sealed interface ReportBugSideEffect {
    data object MaxScreenshotCount : ReportBugSideEffect
    data object Success : ReportBugSideEffect
}
