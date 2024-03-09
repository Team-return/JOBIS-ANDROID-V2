package team.retum.usecase.entity.notification

import android.annotation.SuppressLint
import team.retum.network.model.response.notification.FetchNotificationsResponse
import java.text.SimpleDateFormat

data class NotificationsEntity(
    val notifications: List<NotificationEntity>,
) {
    data class NotificationEntity(
        val notificationId: Long,
        val title: String,
        val content: String,
        val topic: String,
        val detailId: Long,
        val createAt: String,
        val new: Boolean,
    )
}

internal fun FetchNotificationsResponse.toNotificationsEntity() = NotificationsEntity(
    notifications = this.notifications.map { it.toEntity() },
)

@SuppressLint("SimpleDateFormat")
private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")

@SuppressLint("SimpleDateFormat")
private val outputFormat = SimpleDateFormat("yyyy-MM-dd")

private fun FetchNotificationsResponse.NotificationResponse.toEntity() =
    NotificationsEntity.NotificationEntity(
        notificationId = this.notificationId,
        title = this.title,
        content = this.content,
        topic = this.topic,
        detailId = this.detailId,
        createAt = outputFormat.format(inputFormat.parse(this.createdAt)!!),
        new = this.new,
    )
