package team.retum.network.datasource.notice

import team.retum.network.model.response.notice.FetchNoticeDetailsResponse
import team.retum.network.model.response.notice.FetchNoticesResponse

interface NoticeDataSource {
    suspend fun fetchNotices(): FetchNoticesResponse
    suspend fun fetchNoticeDetails(noticeId: Long): FetchNoticeDetailsResponse
}
