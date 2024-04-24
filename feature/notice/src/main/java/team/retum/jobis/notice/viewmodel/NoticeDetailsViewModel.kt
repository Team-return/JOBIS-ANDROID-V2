package team.retum.jobis.notice.viewmodel

import android.content.Context
import android.os.Environment
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    internal fun saveFileData(
        urlString: String,
        destinationPath: String,
        context: Context,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val url = URL(ResourceKeys.IMAGE_URL + urlString)
                val connection = url.openConnection()
                connection.connect()

                // 파일을 저장할 디렉토리 생성
                val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + destinationPath

                // 디렉토리 존재 확인
                val directory = File(dir)
                if (!directory.exists()) {
                    directory.createNewFile()
                }

                // 파일 스트림 초기화
                val input = BufferedInputStream(url.openStream(), 8192)
                val output = FileOutputStream(dir)

                val data = ByteArray(1024)
                var total: Long = 0
                var count: Int

                // 파일 경로 저장(파일 열때 사용)
                setState { state.value.copy(filePath = dir) }

                // 파일 버퍼를 사용하여 다운로드
                while (input.read(data).also { count = it } != -1) {
                    total += count.toLong()
                    output.write(data, 0, count)
                }

                // 스트림 및 열기 닫기
                output.flush()
                output.close()
                input.close()
            }.onSuccess {
                postSideEffect(NoticeDetailsSideEffect.DownLoadSuccess)
            }.onFailure {
                postSideEffect(NoticeDetailsSideEffect.DownloadFailed)
            }
        }
    }
}

internal data class NoticeDetailsState(
    val noticeId: Long,
    val noticeDetailsEntity: NoticeDetailsEntity,
    val filePath: String,
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
            filePath = "",
        )
    }
}

internal sealed interface NoticeDetailsSideEffect {
    data object DownLoadSuccess : NoticeDetailsSideEffect
    data object BadRequest : NoticeDetailsSideEffect
    data object DownloadFailed : NoticeDetailsSideEffect
}
