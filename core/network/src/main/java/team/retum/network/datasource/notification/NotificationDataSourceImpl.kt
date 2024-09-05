package team.retum.network.datasource.notification

import team.retum.common.enums.NotificationTopic
import team.retum.network.api.NotificationApi
import team.retum.network.model.response.notification.FetchNotificationSettingStatusesResponse
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

    override suspend fun settingNotification(topic: NotificationTopic) =
        RequestHandler<Unit>().request {
            notificationApi.settingNotification(topic = topic)
        }

    override suspend fun settingAllNotification() =
        RequestHandler<Unit>().request {
            notificationApi.settingAllNotification()
        }

    override suspend fun fetchNotificationSettingStatuses(): FetchNotificationSettingStatusesResponse =
        RequestHandler<FetchNotificationSettingStatusesResponse>().request {
            notificationApi.fetchNotificationSettingStatuses()
        }
}
