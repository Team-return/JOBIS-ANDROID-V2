package team.retum.data.repository.notice

import team.retum.network.datasource.notice.NoticeDataSource
import team.retum.network.model.response.notice.FetchNoticeDetailsResponse
import team.retum.network.model.response.notice.FetchNoticesCountResponse
import team.retum.network.model.response.notice.FetchNoticesResponse
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(
    private val noticeDataSource: NoticeDataSource,
) : NoticeRepository {
    override suspend fun fetchNotices(page: Long): FetchNoticesResponse =
        noticeDataSource.fetchNotices(page = page)

    override suspend fun fetchNoticesCount(): FetchNoticesCountResponse =
        noticeDataSource.fetchNoticesCount()

    override suspend fun fetchNoticeDetails(noticeId: Long): FetchNoticeDetailsResponse =
        noticeDataSource.fetchNoticeDetails(noticeId = noticeId)
}
