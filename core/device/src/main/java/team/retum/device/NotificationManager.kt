package team.retum.device

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import team.retum.common.utils.notificationPermissionGranted
import team.retum.jobisdesignsystemv2.foundation.JobisIcon

private object Notifications {
    const val NOTIFICATION_CHANNEL_ID = "jobis"
    const val CHANNEL_NAME = "jobis"
    const val CHANNEL_DESCRIPTION = "jobis notification channel"
}

object NotificationData {
    const val TOPIC = "topic"
    const val DETAIL_ID = "detail_id"
}
class NotificationManager(
    private val context: Context,
    private val intentClass: Class<*>,
) {

    init {
        createNotificationChannel()
    }

    private val notificationManagerCompat: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(context)
    }

    @SuppressLint("MissingPermission")
    fun sendNotification(
        title: String?,
        body: String?,
        data: Map<String, String>,
        notificationId: Int,
    ) {
        if (notificationPermissionGranted(context = context)) {
            val intent = Intent(context, intentClass).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                data.forEach { (key, value) -> putExtra(key, value) }
            }
            val contentPendingIntent = PendingIntent.getActivity(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            val notificationBuilder =
                NotificationCompat.Builder(context, Notifications.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(JobisIcon.Notification)
                    .setColor(ContextCompat.getColor(context, R.color.primary))
                    .setAutoCancel(true)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setContentIntent(contentPendingIntent)
                    .apply {
                        title?.let(::setContentTitle)
                        body?.let(::setContentText)
                    }
            notificationManagerCompat.notify(
                notificationId,
                notificationBuilder.build(),
            )
        }
    }

    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
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
