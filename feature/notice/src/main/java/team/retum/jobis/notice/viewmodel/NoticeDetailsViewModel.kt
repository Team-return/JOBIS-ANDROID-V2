package team.retum.jobis.notice.viewmodel

import android.content.Context
import android.os.Environment
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.utils.ResourceKeys
import team.retum.usecase.entity.notice.NoticeDetailsEntity
import team.retum.usecase.usecase.notice.FetchNoticeDetailsUseCase
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject

@HiltViewModel
internal class NoticeDetailsViewModel @Inject constructor(
    private val fetchNoticeDetailsUseCase: FetchNoticeDetailsUseCase,
) : BaseViewModel<NoticeDetailsState, NoticeDetailsSideEffect>(NoticeDetailsState.getDefaultState()) {

    internal fun fetchNoticeDetails(noticeId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchNoticeDetailsUseCase(noticeId = noticeId)
                .onSuccess {
                    setState { state.value.copy(noticeDetailsEntity = it) }
                }
                .onFailure {
                    postSideEffect(NoticeDetailsSideEffect.BadRequest)
                }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    internal fun saveFileData(urlString: String, destinationPath: String, context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL(ResourceKeys.IMAGE_URL + urlString)
                val connection = url.openConnection()
                connection.connect()

                val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + destinationPath

                val directory = File(dir)
                if (!directory.exists()) {
                    directory.createNewFile()
                }

                val input = BufferedInputStream(url.openStream(), 8192)
                val output = FileOutputStream(dir)

                val data = ByteArray(1024)
                var total: Long = 0
                var count: Int
                while (input.read(data).also { count = it } != -1) {
                    total += count.toLong()
                    output.write(data, 0, count)
                }

                output.flush()
                output.close()
                input.close()
            } catch (e: Exception) {
                postSideEffect(NoticeDetailsSideEffect.DownloadFailed)
            }
        }
    }
}

internal data class NoticeDetailsState(
    val noticeId: Long,
    val noticeDetailsEntity: NoticeDetailsEntity,
) {
    companion object {
        fun getDefaultState() = NoticeDetailsState(
            noticeId = 0,
            noticeDetailsEntity = NoticeDetailsEntity(
                title = "",
                content = "",
                createdAt = "",
                attachments = emptyList(),
            ),
        )
    }
}

internal sealed interface NoticeDetailsSideEffect {
    data object BadRequest : NoticeDetailsSideEffect
    data object DownloadFailed : NoticeDetailsSideEffect
}
