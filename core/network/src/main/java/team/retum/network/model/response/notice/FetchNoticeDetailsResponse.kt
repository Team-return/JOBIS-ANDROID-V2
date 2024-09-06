package team.retum.network.model.response.notice

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import team.retum.common.enums.AttachmentType

@JsonClass(generateAdapter = true)
data class FetchNoticeDetailsResponse(
    @Json(name = "title") val title: String,
    @Json(name = "content") val content: String,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "attachments") val attachments: List<AttachmentResponse>,
) {
    @JsonClass(generateAdapter = true)
    data class AttachmentResponse(
        @Json(name = "url") val url: String,
        @Json(name = "type") val type: AttachmentType,
    )
}
