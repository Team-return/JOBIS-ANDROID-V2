package team.retum.network.model.response.notice

import com.google.gson.annotations.SerializedName
import team.retum.common.enums.AttachmentType

data class FetchNoticeDetailsResponse(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("attachments") val attachments: List<AttachmentResponse>,
) {
    data class AttachmentResponse(
        @SerializedName("url") val url: String,
        @SerializedName("type") val type: AttachmentType,
    )
}
