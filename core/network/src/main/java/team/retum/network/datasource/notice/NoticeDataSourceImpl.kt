package team.retum.network.datasource.notice

import team.retum.network.api.NoticeApi
import team.retum.network.model.response.notice.FetchNoticeDetailsResponse
import team.retum.network.model.response.notice.FetchNoticesResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class NoticeDataSourceImpl @Inject constructor(
    private val noticeApi: NoticeApi,
) : NoticeDataSource {
    override suspend fun fetchNotices(): FetchNoticesResponse =
        RequestHandler<FetchNoticesResponse>().request {
            noticeApi.fetchNotices()
        }

    override suspend fun fetchNoticeDetails(noticeId: Long): FetchNoticeDetailsResponse =
        RequestHandler<FetchNoticeDetailsResponse>().request {
            noticeApi.fetchNoticeDetails(noticeId = noticeId)
        }
}
