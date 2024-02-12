package team.retum.network.datasource

import team.retum.network.api.BookmarkApi
import team.retum.network.model.response.BookmarksResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class BookmarkDataSourceImpl @Inject constructor(
    private val bookmarkApi: BookmarkApi,
) : BookmarkDataSource {
    override suspend fun bookmarks(): BookmarksResponse =
        RequestHandler<BookmarksResponse>().request {
            bookmarkApi.bookmarks()
        }

    override suspend fun recruitmentBookmark(recruitmentId: Long) =
        RequestHandler<Unit>().request {
            bookmarkApi.recruitmentBookmark(recruitmentId)
        }
}