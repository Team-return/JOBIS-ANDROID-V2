package team.retum.network.model.response.notification

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import team.retum.common.enums.NotificationTopic

@JsonClass(generateAdapter = true)
data class FetchNotificationSettingStatusesResponse(
    @Json(name = "topics") val topics: List<NotificationSettingStatusResponse>,
) {
    @JsonClass(generateAdapter = true)
    data class NotificationSettingStatusResponse(
        @Json(name = "topic") val topic: NotificationTopic,
        @Json(name = "subscribed") val subscribed: Boolean,
    )
}
