package team.retum.network.model.request.application

import com.google.gson.annotations.SerializedName
import team.retum.common.enums.AttachmentType

data class ApplyCompanyRequest(
    @SerializedName("attachments") val attachments: List<Attachments>,
) {
    data class Attachments(
        @SerializedName("url") val url: String,
        @SerializedName("type") val attachmentType: AttachmentType,
    )
}
