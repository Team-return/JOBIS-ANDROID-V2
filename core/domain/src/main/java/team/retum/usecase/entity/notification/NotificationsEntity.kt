package team.retum.usecase.entity.notification

import androidx.compose.runtime.Immutable
import team.retum.common.enums.AlarmType
import team.retum.network.model.response.notification.FetchNotificationsResponse

data class NotificationsEntity(
    val notifications: List<NotificationEntity>,
) {
    @Immutable
    data class NotificationEntity(
        val notificationId: Long,
        val title: String,
        val content: String,
        val topic: AlarmType?,
        val detailId: Long,
        val createAt: String,
        val new: Boolean,
    )
}

internal fun FetchNotificationsResponse.toNotificationsEntity() = NotificationsEntity(
    notifications = this.notifications.map { it.toEntity() },
)

private val notificationDateRegex = Regex("^\\d{4}-\\d{2}-\\d{2}")

private fun FetchNotificationsResponse.NotificationResponse.toEntity() =
    NotificationsEntity.NotificationEntity(
        notificationId = this.notificationId,
        title = this.title,
        content = this.content,
        topic = AlarmType.entries.firstOrNull { it.name.equals(this.topic, ignoreCase = true) },
        detailId = this.detailId,
        createAt = notificationDateRegex.find(this.createdAt)?.value ?: this.createdAt,
        new = this.new,
    )
