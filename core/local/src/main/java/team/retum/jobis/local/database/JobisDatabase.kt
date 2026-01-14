package team.retum.jobis.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import team.retum.jobis.local.dao.BookmarkDao
import team.retum.jobis.local.entity.BookmarkLocalEntity

@Database(
    entities = [BookmarkLocalEntity::class],
    version = 4,
)
abstract class JobisDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}
