package team.retum.network.datasource.notification

import team.retum.common.enums.NotificationTopic
import team.retum.network.model.response.notification.FetchNotificationsResponse

interface NotificationDataSource {
    suspend fun fetchNotifications(isNew: Boolean?): FetchNotificationsResponse

    suspend fun readNotification(notificationId: Long)

    suspend fun subscribeNotificationTopic(
        deviceToken: String,
        topic: NotificationTopic,
    )

    suspend fun unsubscribeNotificationTopic(
        deviceToken: String,
        topic: NotificationTopic,
    )
}
