package team.retum.usecase.entity.application

import androidx.compose.runtime.Immutable
import team.retum.common.enums.ApplyStatus
import team.retum.common.enums.AttachmentType
import team.retum.network.model.response.application.FetchAppliedCompaniesResponse

data class AppliedCompaniesEntity(
    val applications: List<ApplicationEntity>,
) {
    @Immutable
    data class ApplicationEntity(
        val applicationId: Long,
        val recruitmentId: Long,
        val company: String,
        val companyLogoUrl: String,
        val attachments: List<AttachmentEntity>,
        val applicationStatus: ApplyStatus,
    ) {
        data class AttachmentEntity(
            val url: String,
            val type: AttachmentType,
        )
    }
}

internal fun FetchAppliedCompaniesResponse.toEntity() = AppliedCompaniesEntity(
    applications = this.applications.map { it.toEntity() },
)

private fun FetchAppliedCompaniesResponse.ApplicationResponse.toEntity() =
    AppliedCompaniesEntity.ApplicationEntity(
        applicationId = this.applicationId,
        recruitmentId = this.recruitmentId,
        company = this.company,
        companyLogoUrl = this.companyLogoUrl,
        attachments = this.attachments.map { it.toEntity() },
        applicationStatus = this.applicationStatus,
    )

private fun FetchAppliedCompaniesResponse.ApplicationResponse.AttachmentResponse.toEntity() =
    AppliedCompaniesEntity.ApplicationEntity.AttachmentEntity(
        url = this.url,
        type = this.attachmentType,
    )
