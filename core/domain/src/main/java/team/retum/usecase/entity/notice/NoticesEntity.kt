package team.retum.usecase.entity.notice

import team.retum.network.model.response.notice.FetchNoticesResponse

data class NoticesEntity(
    val notices: List<NoticeEntity>,
) {
    data class NoticeEntity(
        val id: Long,
        val title: String,
        val createdAt: String,
    )
}

internal fun FetchNoticesResponse.toNoticesEntity() = NoticesEntity(
    notices = this.notices.map { it.toEntity() },
)

private fun FetchNoticesResponse.NoticeResponse.toEntity() =
    NoticesEntity.NoticeEntity(
        id = this.id,
        title = this.title,
        createdAt = this.createdAt,
    )
