package team.retum.jobis.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import team.retum.jobis.local.entity.BookmarkLocalEntity

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmarks WHERE recruitmentId = :recruitmentId")
    fun observeBookmark(recruitmentId: Long): Flow<BookmarkLocalEntity?>

    @Query("SELECT * FROM bookmarks WHERE isBookmarked = 1 ORDER BY updatedAt DESC")
    fun observeAllBookmarks(): Flow<List<BookmarkLocalEntity>>

    @Query("SELECT isBookmarked FROM bookmarks WHERE recruitmentId = :recruitmentId")
    suspend fun isBookmarked(recruitmentId: Long): Boolean?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookmark: BookmarkLocalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bookmarks: List<BookmarkLocalEntity>)

    @Query("DELETE FROM bookmarks")
    suspend fun deleteAll()
}
