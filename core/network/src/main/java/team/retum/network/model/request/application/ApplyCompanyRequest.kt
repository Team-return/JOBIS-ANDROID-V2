package team.retum.network.model.request.application

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import team.retum.common.enums.AttachmentType

@JsonClass(generateAdapter = true)
data class ApplyCompanyRequest(
    @Json(name = "attachments") val attachments: List<Attachments>,
) {
    data class Attachments(
        @Json(name = "url") val url: String,
        @Json(name = "type") val attachmentType: AttachmentType,
    )
}
