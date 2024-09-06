package team.retum.data.repository.notification

import team.retum.common.enums.NotificationTopic
import team.retum.network.datasource.notification.NotificationDataSource
import team.retum.network.model.response.notification.FetchNotificationSettingStatusesResponse
import team.retum.network.model.response.notification.FetchNotificationsResponse
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDataSource: NotificationDataSource,
) : NotificationRepository {
    override suspend fun fetchNotifications(isNew: Boolean?): FetchNotificationsResponse =
        notificationDataSource.fetchNotifications(isNew = isNew)

    override suspend fun readNotification(notificationId: Long) =
        notificationDataSource.readNotification(notificationId = notificationId)

    override suspend fun settingNotification(topic: NotificationTopic) =
        notificationDataSource.settingNotification(topic = topic)

    override suspend fun settingAllNotification() =
        notificationDataSource.settingAllNotification()

    override suspend fun fetchNotificationSettingStatuses(): FetchNotificationSettingStatusesResponse =
        notificationDataSource.fetchNotificationSettingStatuses()
}
