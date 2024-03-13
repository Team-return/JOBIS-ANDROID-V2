package team.retum.jobis.local.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import team.retum.common.utils.ResourceKeys
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferenceModule {

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context,
    ): SharedPreferences {
        return context.getSharedPreferences(ResourceKeys.SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    }
}
