package team.retum.common.model

data class ReApplyData(
    val recruitmentId: Long = 0,
    val rejectionReason: String = "",
    val companyLogoUrl: String = "",
    val companyName: String = "",
)
