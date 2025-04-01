package team.retum.device

import android.util.Log
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
                Log.d("DeviceTokenManager", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            CoroutineScope(Dispatchers.IO).launch {
                saveDeviceToken(deviceToken = task.result)
            }
        }
    }
}
