package team.retum.jobis.local.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import team.retum.common.utils.ResourceKeys
import team.retum.jobis.local.database.JobisDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): RoomDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = JobisDatabase::class.java,
            name = ResourceKeys.DATABASE_NAME,
        ).build()
    }
}
