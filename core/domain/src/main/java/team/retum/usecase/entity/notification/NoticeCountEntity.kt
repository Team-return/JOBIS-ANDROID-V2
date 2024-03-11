package team.retum.usecase.entity.notification

import team.retum.network.model.response.notice.FetchNoticePageCountResponse

data class NoticeCountEntity (
    val totalPageCount: Long,
)

internal fun FetchNoticePageCountResponse.toNoticeCountEntity() = NoticeCountEntity(
    totalPageCount = this.totalPageCount,
)
