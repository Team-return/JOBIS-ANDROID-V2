package team.retum.jobis.local.datasource.bookmark

import kotlinx.coroutines.flow.Flow
import team.retum.jobis.local.entity.BookmarkLocalEntity

interface LocalBookmarkDataSource {
    fun observeBookmark(recruitmentId: Long): Flow<BookmarkLocalEntity?>

    fun observeAllBookmarks(): Flow<List<BookmarkLocalEntity>>

    suspend fun isBookmarked(recruitmentId: Long): Boolean?

    suspend fun insert(bookmark: BookmarkLocalEntity)

    suspend fun insertAll(bookmarks: List<BookmarkLocalEntity>)

    suspend fun deleteAll()
}
