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
import team.retum.jobis.local.DeviceDataSource
import team.retum.jobis.local.UserDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferenceModule {

    @Provides
    @Singleton
    @UserDataSource
    fun provideDataStore(
        @ApplicationContext context: Context,
    ): SharedPreferences {
        return context.getSharedPreferences(ResourceKeys.USER_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    }

    @Provides
    @Singleton
    @DeviceDataSource
    fun provideDeviceDataStore(
        @ApplicationContext context: Context,
    ): SharedPreferences {
        return context.getSharedPreferences(ResourceKeys.DEVICE_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    }
}
