package team.retum.jobis.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import team.retum.jobis.local.dao.BookmarkDao
import team.retum.jobis.local.dao.CodeDao
import team.retum.jobis.local.entity.BookmarkLocalEntity
import team.retum.jobis.local.entity.CodeLocalEntity

@Database(
    entities = [
        BookmarkLocalEntity::class,
        CodeLocalEntity::class,
    ],
    version = 5,
)
abstract class JobisDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun codeDao(): CodeDao
}
