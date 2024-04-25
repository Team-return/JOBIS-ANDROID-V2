package team.retum.jobis.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import team.retum.jobis.local.entity.TestLocalEntity

@Database(
    entities = [TestLocalEntity::class],
    version = 1,
)
abstract class JobisDatabase : RoomDatabase()
