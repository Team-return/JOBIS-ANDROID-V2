package team.retum.data.repository

import team.retum.network.datasource.BookmarkDataSource
import team.retum.network.model.response.BookmarksResponse
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkDataSource: BookmarkDataSource,
) : BookmarkRepository {
    override suspend fun fetchBookmarks(): BookmarksResponse =
        bookmarkDataSource.fetchBookmark()

    override suspend fun bookmarkRecruitment(recruitmentId: Long) =
        bookmarkDataSource.bookmarkRecruitment(recruitmentId)
}
