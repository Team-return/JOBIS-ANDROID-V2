package team.retum.jobis.local.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import team.retum.common.utils.ResourceKeys
import team.retum.jobis.local.dao.BookmarkDao
import team.retum.jobis.local.dao.CodeDao
import team.retum.jobis.local.database.JobisDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `codes` (
                    `code` INTEGER NOT NULL,
                    `keyword` TEXT NOT NULL,
                    `type` TEXT NOT NULL,
                    `parentCode` INTEGER,
                    `cachedAt` INTEGER NOT NULL,
                    PRIMARY KEY(`code`)
                )
                """.trimIndent(),
            )
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): JobisDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = JobisDatabase::class.java,
            name = ResourceKeys.DATABASE_NAME,
        )
            .addMigrations(MIGRATION_4_5)
            .build()
    }

    @Provides
    @Singleton
    fun provideBookmarkDao(database: JobisDatabase): BookmarkDao =
        database.bookmarkDao()

    @Provides
    @Singleton
    fun provideCodeDao(database: JobisDatabase): CodeDao =
        database.codeDao()
}
