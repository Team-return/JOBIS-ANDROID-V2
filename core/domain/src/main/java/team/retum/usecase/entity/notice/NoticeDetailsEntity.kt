package team.retum.usecase.entity.notice

import team.retum.common.enums.AttachmentType
import team.retum.network.model.response.notice.FetchNoticeDetailsResponse

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

internal fun FetchNoticeDetailsResponse.toEntity() = NoticeDetailsEntity(
    title = this.title,
    content = this.content,
    createdAt = this.createdAt,
    attachments = this.attachments.map { it.toEntity() },
)

private fun FetchNoticeDetailsResponse.AttachmentResponse.toEntity() =
    NoticeDetailsEntity.AttachmentEntity(
        url = this.url,
        type = this.type,
    )
