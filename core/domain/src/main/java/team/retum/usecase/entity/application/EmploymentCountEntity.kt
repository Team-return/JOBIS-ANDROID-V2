package team.retum.usecase.entity.application

import team.retum.network.model.response.application.FetchEmploymentCountResponse

data class EmploymentCountEntity(
    val totalStudentCount: Long,
    val passCount: Long,
    val approvedCount: Long,
)

internal fun FetchEmploymentCountResponse.toEntity() = EmploymentCountEntity(
    totalStudentCount = this.totalStudentCount,
    passCount = this.passCount,
    approvedCount = this.approvedCount,
)
