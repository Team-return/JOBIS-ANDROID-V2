package team.retum.network.datasource.notification

import team.retum.common.enums.NotificationTopic
import team.retum.network.model.response.notification.FetchNotificationSettingStatusesResponse
import team.retum.network.model.response.notification.FetchNotificationsResponse

interface NotificationDataSource {
    suspend fun fetchNotifications(isNew: Boolean?): FetchNotificationsResponse

    suspend fun readNotification(notificationId: Long)

    suspend fun settingNotification(topic: NotificationTopic)

    suspend fun settingAllNotification()

    suspend fun fetchNotificationSettingStatuses(): FetchNotificationSettingStatusesResponse
}
