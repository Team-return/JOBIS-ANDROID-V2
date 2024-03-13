package team.retum.notification.util

import team.retum.usecase.usecase.user.SaveDeviceTokenUseCase
import javax.inject.Inject

class DeviceTokenManager @Inject constructor(
    private val saveDeviceTokenUseCase: SaveDeviceTokenUseCase,
) {
    suspend fun saveDeviceToken(deviceToken: String) {
        saveDeviceTokenUseCase(deviceToken = deviceToken)
    }
}
