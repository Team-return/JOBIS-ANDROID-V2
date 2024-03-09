package team.retum.data.repository.notification

import team.retum.network.model.response.notification.FetchNotificationsResponse

interface NotificationRepository {
    suspend fun fetchNotifications(isNew: Boolean?): FetchNotificationsResponse
    suspend fun readNotification(notificationId: Long)
}
