package team.retum.notification.util

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.usecase.usecase.user.SaveDeviceTokenUseCase
import javax.inject.Inject

class DeviceTokenManager @Inject constructor(
    private val saveDeviceTokenUseCase: SaveDeviceTokenUseCase,
) {
    suspend fun saveDeviceToken(deviceToken: String) {
        saveDeviceTokenUseCase(deviceToken = deviceToken)
    }

    suspend fun fetchDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                throw DeviceTokenException()
            }
            CoroutineScope(Dispatchers.IO).launch {
                saveDeviceToken(deviceToken = task.result)
            }
        }
    }
}
