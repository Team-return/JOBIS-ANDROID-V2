package team.retum.network.model.response.notice

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchNoticesResponse(
    @Json(name = "notices") val notices: List<NoticeResponse>,
) {
    data class NoticeResponse(
        @Json(name = "id") val id: Long,
        @Json(name = "title") val title: String,
        @Json(name = "created_at") val createdAt: String,
    )
}
