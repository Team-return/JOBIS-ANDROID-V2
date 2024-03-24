package team.retum.common.model

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationData(
    val applicationId: Long,
    val recruitmentId: Long,
    val rejectionReason: String,
    val companyLogoUrl: String,
    val companyName: String,
    val isReApply: Boolean,
) {
    companion object {
        fun getDefaultApplicationData() = ApplicationData(
            applicationId = 0,
            recruitmentId = 0,
            rejectionReason = "",
            companyLogoUrl = "",
            companyName = "",
            isReApply = false,
        )
    }
}
