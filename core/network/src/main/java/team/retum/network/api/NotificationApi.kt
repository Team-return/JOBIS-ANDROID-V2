package team.retum.network.api

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import team.retum.common.enums.NotificationTopic
import team.retum.network.di.RequestUrls
import team.retum.network.model.response.notification.FetchNotificationsResponse

interface NotificationApi {

    @GET(RequestUrls.Notification.notifications)
    suspend fun fetchNotifications(
        @Query("is_new") isNew: Boolean?,
    ): FetchNotificationsResponse

    @PATCH(RequestUrls.Notification.notification)
    suspend fun readNotification(
        @Path("notification-id") notificationId: Long,
    )

    @POST(RequestUrls.Notification.topic)
    suspend fun subscribeNotificationTopic(
        @Query("token") deviceToken: String,
        @Query("topic") topic: NotificationTopic,
    )

    @DELETE(RequestUrls.Notification.topic)
    suspend fun unsubscribeNotificationTopic(
        @Query("token") deviceToken: String,
        @Query("topic") topic: NotificationTopic,
    )
}
