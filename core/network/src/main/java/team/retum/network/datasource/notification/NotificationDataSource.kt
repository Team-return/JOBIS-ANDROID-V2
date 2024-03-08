package team.retum.network.datasource.notification

import team.retum.network.model.response.notification.FetchNotificationsResponse

interface NotificationDataSource {
    suspend fun fetchNotifications(isNew: Boolean?) : FetchNotificationsResponse
    suspend fun fetchNotificationRead(notificationId: Long)
}
