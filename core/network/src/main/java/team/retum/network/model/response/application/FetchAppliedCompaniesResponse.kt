package team.retum.network.model.response.application

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import team.retum.common.enums.ApplyStatus
import team.retum.common.enums.AttachmentType

@JsonClass(generateAdapter = true)
data class FetchAppliedCompaniesResponse(
    @Json(name = "applications") val applications: List<ApplicationResponse>,
) {
    @JsonClass(generateAdapter = true)
    data class ApplicationResponse(
        @Json(name = "application_id") val applicationId: Long,
        @Json(name = "recruitment_id") val recruitmentId: Long,
        @Json(name = "company") val company: String,
        @Json(name = "company_logo_url") val companyLogoUrl: String,
        @Json(name = "attachments") val attachments: List<AttachmentResponse>,
        @Json(name = "application_status") val applicationStatus: ApplyStatus,
    ) {
        @JsonClass(generateAdapter = true)
        data class AttachmentResponse(
            @Json(name = "url") val url: String,
            @Json(name = "type") val attachmentType: AttachmentType,
        )
    }
}
