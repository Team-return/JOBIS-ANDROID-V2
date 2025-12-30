package team.retum.data.repository.bookmark

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import team.retum.jobis.local.datasource.bookmark.LocalBookmarkDataSource
import team.retum.jobis.local.entity.BookmarkLocalEntity
import team.retum.network.datasource.bookmark.BookmarkDataSource
import team.retum.network.model.response.BookmarksResponse
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkDataSource: BookmarkDataSource,
    private val localDataSource: LocalBookmarkDataSource,
) : BookmarkRepository {

    override suspend fun fetchBookmarks(): BookmarksResponse =
        bookmarkDataSource.fetchBookmark()

    override suspend fun bookmarkRecruitment(recruitmentId: Long) =
        bookmarkDataSource.bookmarkRecruitment(recruitmentId)

    override fun observeBookmarkStatus(recruitmentId: Long): Flow<Boolean> {
        return localDataSource.observeBookmark(recruitmentId)
            .map { it?.isBookmarked ?: false }
    }

    override fun observeAllBookmarks(): Flow<List<BookmarkLocalEntity>> {
        return localDataSource.observeAllBookmarks()
    }

    override suspend fun toggleBookmark(recruitmentId: Long): Result<Unit> {
        return runCatching {
            val currentStatus = localDataSource.isBookmarked(recruitmentId) ?: false
            val newStatus = !currentStatus

            localDataSource.insert(
                BookmarkLocalEntity(
                    recruitmentId = recruitmentId,
                    isBookmarked = newStatus,
                    updatedAt = System.currentTimeMillis(),
                ),
            )

            try {
                bookmarkDataSource.bookmarkRecruitment(recruitmentId)
            } catch (e: Exception) {
                localDataSource.insert(
                    BookmarkLocalEntity(
                        recruitmentId = recruitmentId,
                        isBookmarked = currentStatus,
                        updatedAt = System.currentTimeMillis(),
                    ),
                )
                throw e
            }
        }
    }

    override suspend fun syncBookmarksFromServer(): Result<Unit> {
        return runCatching {
            val response = bookmarkDataSource.fetchBookmark()
            val bookmarks = response.bookmarks.map {
                BookmarkLocalEntity(
                    recruitmentId = it.recruitmentId,
                    companyLogoUrl = it.companyLogoUrl,
                    companyName = it.companyName,
                    createdAt = it.createdAt,
                    isBookmarked = true,
                    updatedAt = System.currentTimeMillis(),
                )
            }

            localDataSource.deleteAll()
            localDataSource.insertAll(bookmarks)
        }
    }
}
