package team.retum.usecase.entity.notification

import team.retum.network.model.response.notification.FetchNotificationsResponse

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

private fun FetchNotificationsResponse.NotificationResponse.toEntity() =
    NotificationsEntity.NotificationEntity(
        notificationId = this.notificationId,
        title = this.title,
        content = this.content,
        topic = this.topic,
        detailId = this.detailId,
        createAt = this.createdAt,
        new = this.new,
    )
