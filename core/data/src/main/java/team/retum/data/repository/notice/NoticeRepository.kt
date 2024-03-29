package team.retum.data.repository.notice

import team.retum.network.model.response.notice.FetchNoticeDetailsResponse
import team.retum.network.model.response.notice.FetchNoticesResponse

interface NoticeRepository {
    suspend fun fetchNotices(): FetchNoticesResponse
    suspend fun fetchNoticeDetails(noticeId: Long): FetchNoticeDetailsResponse
}
