package team.retum.data.repository.notification

import team.retum.network.datasource.notification.NotificationDataSource
import team.retum.network.model.response.notification.FetchNotificationsResponse
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDataSource: NotificationDataSource,
) : NotificationRepository {
    override suspend fun fetchNotifications(isNew: Boolean?): FetchNotificationsResponse =
        notificationDataSource.fetchNotifications(isNew = isNew)

    override suspend fun fetchNotificationRead(notificationId: Long) =
        notificationDataSource.fetchNotificationRead(notificationId = notificationId)
}
