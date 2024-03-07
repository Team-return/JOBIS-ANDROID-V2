package team.retum.network.datasource.notification

import team.retum.network.api.NotificationApi
import team.retum.network.model.response.notification.FetchNotificationsResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class NotificationDataSourceImpl @Inject constructor(
    private val notificationApi: NotificationApi,
) : NotificationDataSource {
    override suspend fun fetchNotifications(isNew: Boolean): FetchNotificationsResponse =
        RequestHandler<FetchNotificationsResponse>().request {
            notificationApi.fetchNotifications(isNew = isNew)
        }

    override suspend fun fetchNotificationRead(notificationId: Long) =
        RequestHandler<Unit>().request {
            notificationApi.fetchNotificationRead(notificationId = notificationId)
        }
}
