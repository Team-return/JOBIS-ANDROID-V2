package team.retum.network.datasource.bookmark

import team.retum.network.api.BookmarkApi
import team.retum.network.model.response.BookmarksResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class BookmarkDataSourceImpl @Inject constructor(
    private val bookmarkApi: BookmarkApi,
) : BookmarkDataSource {
    override suspend fun fetchBookmark(): BookmarksResponse =
        RequestHandler<BookmarksResponse>().request {
            bookmarkApi.fetchBookmarks()
        }

    override suspend fun bookmarkRecruitment(recruitmentId: Long) =
        RequestHandler<Unit>().request {
            bookmarkApi.bookmarkRecruitment(recruitmentId)
        }
}
