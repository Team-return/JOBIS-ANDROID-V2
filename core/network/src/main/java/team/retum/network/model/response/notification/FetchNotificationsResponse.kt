package team.retum.network.model.response.notification

import com.google.gson.annotations.SerializedName
import team.retum.common.enums.AlarmType

data class FetchNotificationsResponse(
    @SerializedName("notifications") val notifications: List<NotificationResponse>
) {
    data class NotificationResponse(
        @SerializedName("notification_id") val notificationId: Long,
        @SerializedName("title") val title: String,
        @SerializedName("content") val content: String,
        @SerializedName("topic") val topic: AlarmType,
        @SerializedName("detail_id") val detailId: Long,
        @SerializedName("created_at") val createdAt: String,
        @SerializedName("new") val new: Boolean,
    )
}
