package team.retum.jobis.application.viewmodel

import android.content.ClipData
import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import dagger.hilt.android.lifecycle.HiltViewModel
import team.retum.common.base.BaseViewModel
import team.retum.common.utils.FileUtil
import java.io.File
import javax.inject.Inject

internal const val MAX_ATTACHMENT_COUNT = 3

@HiltViewModel
internal class ApplicationViewModel @Inject constructor(

) : BaseViewModel<ApplicationState, ApplicationSideEffect>(ApplicationState.getInitialState()) {

    private val attachments: SnapshotStateList<File> = mutableStateListOf()
    private val urls: SnapshotStateList<String> = mutableStateListOf()

    internal fun getAttachments() = attachments
    internal fun getUrls() = urls

    internal fun addAttachment(
        activityResult: ActivityResult,
        context: Context,
    ) {
        val result = activityResult.data
        result?.run {
            clipData?.run {
                addUriToAttachments(
                    uris = this,
                    context = context,
                )
            }
            data?.run {
                if (attachments.size >= MAX_ATTACHMENT_COUNT) {
                    postSideEffect(ApplicationSideEffect.ExceedFileCount)
                } else {
                    addUri(
                        uri = this,
                        context = context,
                    )
                }
            }
        }
        setButtonEnabled()
    }

    private fun addUriToAttachments(
        uris: ClipData,
        context: Context,
    ) {
        val count = uris.itemCount
        if (attachments.size + count > MAX_ATTACHMENT_COUNT) {
            postSideEffect(ApplicationSideEffect.ExceedFileCount)
        } else {
            repeat(uris.itemCount) {
                val uri = uris.getItemAt(it).uri
                addUri(
                    uri = uri,
                    context = context,
                )
            }
        }
    }

    private fun addUri(
        uri: Uri,
        context: Context,
    ) {
        attachments.add(
            FileUtil.toFile(
                context = context,
                uri = uri,
            ),
        )
    }

    internal fun removeAttachment(index: Int) {
        attachments.removeAt(index)
        setButtonEnabled()
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

    private fun setButtonEnabled() = setState {
        state.value.copy(buttonEnabled = attachments.isNotEmpty())
    }
}

internal data class ApplicationState(
    val buttonEnabled: Boolean,
) {
    companion object {
        fun getInitialState() = ApplicationState(
            buttonEnabled = false,
        )
    }
}

internal sealed interface ApplicationSideEffect {
    data object ExceedFileCount : ApplicationSideEffect
}
