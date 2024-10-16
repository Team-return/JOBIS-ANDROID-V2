package team.retum.device.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import team.retum.device.DeviceTokenManager
import team.retum.usecase.usecase.user.SaveDeviceTokenUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TokenManagerModule {
    @Provides
    @Singleton
    fun provideTokenManager(saveDeviceTokenUseCase: SaveDeviceTokenUseCase): DeviceTokenManager {
        return DeviceTokenManager(saveDeviceTokenUseCase = saveDeviceTokenUseCase)
    }
}
