package team.retum.notification.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import team.retum.common.utils.notificationPermissionGranted
import team.retum.jobisdesignsystemv2.foundation.JobisIcon

private object Notifications {
    const val NOTIFICATION_ID = 0
    const val NOTIFICATION_CHANNEL_ID = "jobis"
    const val CHANNEL_NAME = "jobis"
    const val CHANNEL_DESCRIPTION = "jobis notification channel"
}

class NotificationManager(private val context: Context) {

    init {
        createNotificationChannel()
    }

    private val notificationManagerCompat: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(context)
    }

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(context, Notifications.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(JobisIcon.AppLogo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }

    fun setNotificationContent(
        title: String?,
        body: String?,
    ) {
        notificationBuilder.run {
            title?.run { setContentTitle(this) }
            body?.run { setContentText(this) }
        }
    }

    @SuppressLint("MissingPermission")
    fun sendNotification() {
        if (notificationPermissionGranted(context = context)) {
            notificationManagerCompat.notify(
                Notifications.NOTIFICATION_ID,
                notificationBuilder.build(),
            )
        } else {
            // TODO show toast message
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
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
