package team.retum.network.model.response.notification

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import team.retum.common.enums.AlarmType

@JsonClass(generateAdapter = true)
data class FetchNotificationsResponse(
    @Json(name = "notifications") val notifications: List<NotificationResponse>
) {
    @JsonClass(generateAdapter = true)
    data class NotificationResponse(
        @Json(name = "notification_id") val notificationId: Long,
        @Json(name = "title") val title: String,
        @Json(name = "content") val content: String,
        @Json(name = "topic") val topic: AlarmType,
        @Json(name = "detail_id") val detailId: Long,
        @Json(name = "created_at") val createdAt: String,
        @Json(name = "new") val new: Boolean,
    )
}
