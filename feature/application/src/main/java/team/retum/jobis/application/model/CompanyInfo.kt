package team.retum.jobis.application.model

import kotlinx.serialization.Serializable

@Serializable
data class CompanyInfo(
    val companyProfileUrl: String = "",
    val companyName: String = "",
)
