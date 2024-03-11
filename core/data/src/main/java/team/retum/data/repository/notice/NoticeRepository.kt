package team.retum.data.repository.notice

import team.retum.network.model.response.notice.FetchNoticeDetailsResponse
import team.retum.network.model.response.notice.FetchNoticePageCountResponse
import team.retum.network.model.response.notice.FetchNoticesResponse

interface NoticeRepository {
    suspend fun fetchNotices(page: Int): FetchNoticesResponse
    suspend fun fetchNoticeDetails(noticeId: Long): FetchNoticeDetailsResponse
    suspend fun fetchPageCount(title: String?): FetchNoticePageCountResponse
}
