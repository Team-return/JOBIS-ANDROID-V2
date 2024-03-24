package team.retum.common.model

data class ReApplyData(
    val applicationId: Long = 0,
    val recruitmentId: Long = 0,
    val rejectionReason: String = "",
    val companyLogoUrl: String = "",
    val companyName: String = "",
)
