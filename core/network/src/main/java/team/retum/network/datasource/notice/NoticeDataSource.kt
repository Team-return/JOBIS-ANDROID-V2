package team.retum.network.datasource.notice

import team.retum.network.model.response.notice.FetchNoticesResponse

interface NoticeDataSource {
    suspend fun fetchNotices(page: Int) : FetchNoticesResponse
}
