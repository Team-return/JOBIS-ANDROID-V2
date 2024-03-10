package team.retum.data.repository.notice

import team.retum.network.model.response.notice.FetchNoticesResponse

interface NoticeRepository {
    suspend fun fetchNotices(page: Int): FetchNoticesResponse
}
