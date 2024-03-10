package team.retum.bug.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import dagger.hilt.android.lifecycle.HiltViewModel
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.DevelopmentArea
import javax.inject.Inject

private const val MAX_SCREENSHOT_COUNT = 5

@HiltViewModel
internal class ReportBugViewModel @Inject constructor(

) : BaseViewModel<ReportBugState, ReportBugSideEffect>(ReportBugState.getInitialState()) {

    private val screenshots: SnapshotStateList<Uri> = mutableStateListOf()

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

    internal fun onNextClick() {

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
}
