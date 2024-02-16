package team.retum.usecase.entity

import team.retum.network.model.response.CompanyCountResponse

data class CompanyCountEntity(
    val totalPageCount: Long,
)

fun CompanyCountResponse.toCompanyCountEntity() = CompanyCountEntity(
    totalPageCount = this.totalPageCount,
)