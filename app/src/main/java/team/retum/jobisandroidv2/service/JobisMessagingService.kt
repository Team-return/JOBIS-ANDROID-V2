package team.retum.jobisandroidv2.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.notification.util.DeviceTokenManager
import javax.inject.Inject

@AndroidEntryPoint
class JobisMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var deviceTokenManager: DeviceTokenManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            deviceTokenManager.saveDeviceToken(deviceToken = token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }
}
