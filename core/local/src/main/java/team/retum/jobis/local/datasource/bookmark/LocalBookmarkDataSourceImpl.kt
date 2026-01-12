package team.retum.jobis.local.datasource.bookmark

import kotlinx.coroutines.flow.Flow
import team.retum.jobis.local.dao.BookmarkDao
import team.retum.jobis.local.entity.BookmarkLocalEntity
import javax.inject.Inject

class LocalBookmarkDataSourceImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao,
) : LocalBookmarkDataSource {
    override fun observeBookmark(recruitmentId: Long): Flow<BookmarkLocalEntity?> =
        bookmarkDao.observeBookmark(recruitmentId)

    override fun observeAllBookmarks(): Flow<List<BookmarkLocalEntity>> =
        bookmarkDao.observeAllBookmarks()

    override suspend fun isBookmarked(recruitmentId: Long): Boolean? =
        bookmarkDao.isBookmarked(recruitmentId)

    override suspend fun insert(bookmark: BookmarkLocalEntity) =
        bookmarkDao.insert(bookmark)

    override suspend fun insertAll(bookmarks: List<BookmarkLocalEntity>) =
        bookmarkDao.insertAll(bookmarks)

    override suspend fun deleteAll() =
        bookmarkDao.deleteAll()
}
