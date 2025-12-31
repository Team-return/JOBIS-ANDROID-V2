package team.retum.data.repository.bookmark

import kotlinx.coroutines.flow.Flow
import team.retum.jobis.local.entity.BookmarkLocalEntity
import team.retum.network.model.response.BookmarksResponse

interface BookmarkRepository {
    suspend fun fetchBookmarks(): BookmarksResponse

    suspend fun bookmarkRecruitment(recruitmentId: Long)

    fun observeBookmarkStatus(recruitmentId: Long): Flow<Boolean>
    fun observeAllBookmarks(): Flow<List<BookmarkLocalEntity>>
    suspend fun toggleBookmark(recruitmentId: BookmarkLocalEntity): Result<Unit>
    suspend fun syncBookmarksFromServer(): Result<Unit>
}
