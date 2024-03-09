package team.retum.network.datasource.notice

import team.retum.network.api.NoticeApi
import team.retum.network.model.response.notice.FetchNoticesResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class NoticeDataSourceImpl @Inject constructor(
    private val noticeApi: NoticeApi,
) : NoticeDataSource {
    override suspend fun fetchNotices(page: Int): FetchNoticesResponse =
        RequestHandler<FetchNoticesResponse>().request {
            noticeApi.fetchNotices(page = page)
        }
}
