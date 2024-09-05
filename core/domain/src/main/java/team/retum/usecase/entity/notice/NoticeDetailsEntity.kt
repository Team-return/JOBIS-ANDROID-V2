package team.retum.usecase.entity.notice

import android.annotation.SuppressLint
import androidx.compose.runtime.Immutable
import team.retum.common.enums.AttachmentType
import team.retum.network.model.response.notice.FetchNoticeDetailsResponse
import java.text.SimpleDateFormat

@Immutable
data class NoticeDetailsEntity(
    val title: String,
    val content: String,
    val createdAt: String,
    val attachments: List<AttachmentEntity>,
) {
    data class AttachmentEntity(
        val url: String,
        val type: AttachmentType,
    )
}

@SuppressLint("SimpleDateFormat")
private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

@SuppressLint("SimpleDateFormat")
private val outputFormat = SimpleDateFormat("yyyy-MM-dd")

internal fun FetchNoticeDetailsResponse.toEntity() = NoticeDetailsEntity(
    title = this.title,
    content = this.content,
    createdAt = outputFormat.format(inputFormat.parse(this.createdAt)!!),
    attachments = this.attachments.map { it.toEntity() },
)

private fun FetchNoticeDetailsResponse.AttachmentResponse.toEntity() =
    NoticeDetailsEntity.AttachmentEntity(
        url = this.url,
        type = this.type,
    )
