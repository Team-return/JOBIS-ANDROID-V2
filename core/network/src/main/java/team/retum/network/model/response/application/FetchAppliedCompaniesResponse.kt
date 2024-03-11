package team.retum.network.model.response.application

import com.google.gson.annotations.SerializedName
import team.retum.common.enums.ApplyStatus
import team.retum.common.enums.AttachmentType

data class FetchAppliedCompaniesResponse(
    @SerializedName("applications") val applications: List<ApplicationResponse>,
) {
    data class ApplicationResponse(
        @SerializedName("application_id") val applicationId: Long,
        @SerializedName("recruitment_id") val recruitmentId: Long,
        @SerializedName("company") val company: String,
        @SerializedName("company_logo_url") val companyLogoUrl: String,
        @SerializedName("attachments") val attachments: List<AttachmentResponse>,
        @SerializedName("application_status") val applicationStatus: ApplyStatus,
    ) {
        data class AttachmentResponse(
            @SerializedName("url") val url: String,
            @SerializedName("type") val attachmentType: AttachmentType,
        )
    }
}
