package team.retum.usecase.entity.notice

import team.retum.network.model.response.notice.FetchNoticesResponse

data class NoticesEntity (
    val notices: List<NoticeEntity>
) {
    data class NoticeEntity(
        val noticeId: Long,
        val title: String,
        val createAt: String,
    )
}

internal fun FetchNoticesResponse.toNoticesEntity() = NoticesEntity(
    notices = this.notices.map { it.toEntity() }
)

private fun FetchNoticesResponse.NoticeResponse.toEntity() =
        NoticesEntity.NoticeEntity(
            noticeId = this.noticeId,
            title = this.title,
            createAt = this.createAt
        )
