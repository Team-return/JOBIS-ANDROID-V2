package team.retum.data.repository.notice

import team.retum.network.datasource.notice.NoticeDataSource
import team.retum.network.model.response.notice.FetchNoticesResponse
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(
    private val noticeDataSource: NoticeDataSource,
) : NoticeRepository {
    override suspend fun fetchNotices(page: Int): FetchNoticesResponse =
        noticeDataSource.fetchNotices(page = page)
}
