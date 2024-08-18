package team.retum.network.datasource.notification

import team.retum.common.enums.NotificationTopic
import team.retum.network.api.NotificationApi
import team.retum.network.model.response.notification.FetchNotificationsResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class NotificationDataSourceImpl @Inject constructor(
    private val notificationApi: NotificationApi,
) : NotificationDataSource {
    override suspend fun fetchNotifications(isNew: Boolean?): FetchNotificationsResponse =
        RequestHandler<FetchNotificationsResponse>().request {
            notificationApi.fetchNotifications(isNew = isNew)
        }

    override suspend fun readNotification(notificationId: Long) =
        RequestHandler<Unit>().request {
            notificationApi.readNotification(notificationId = notificationId)
        }

    override suspend fun subscribeNotificationTopic(
        deviceToken: String,
        topic: NotificationTopic,
    ) = RequestHandler<Unit>().request {
        notificationApi.subscribeNotificationTopic(
            deviceToken = deviceToken,
            topic = topic,
        )
    }

    override suspend fun unsubscribeNotificationTopic(
        deviceToken: String,
        topic: NotificationTopic,
    ) = RequestHandler<Unit>().request {
        notificationApi.unsubscribeNotificationTopic(
            deviceToken = deviceToken,
            topic = topic,
        )
    }
}
