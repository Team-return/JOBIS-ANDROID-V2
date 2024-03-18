package team.retum.jobisandroidv2.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.notification.util.DeviceTokenManager
import javax.inject.Inject

private object Notifications {
    const val NOTIFICATION_ID = 0
    const val NOTIFICATION_CHANNEL_ID = "jobis"
    const val CHANNEL_NAME = "jobis"
    const val CHANNEL_DESCRIPTION = "jobis notification channel"
}

@AndroidEntryPoint
class JobisMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var deviceTokenManager: DeviceTokenManager

    private val notificationManager: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(applicationContext)
    }

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(applicationContext, Notifications.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(JobisIcon.AppLogo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            deviceTokenManager.saveDeviceToken(deviceToken = token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        createNotificationChannel()
        message.notification?.run {
            title?.run { notificationBuilder.setContentTitle(this) }
            body?.run { notificationBuilder.setContentText(this) }
        }
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(Notifications.NOTIFICATION_ID, notificationBuilder.build())
        } else {

        }
    }

    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel =
            NotificationChannel(
                Notifications.NOTIFICATION_CHANNEL_ID,
                Notifications.CHANNEL_NAME,

                importance,
            ).apply {
                this.description = Notifications.CHANNEL_DESCRIPTION
            }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
