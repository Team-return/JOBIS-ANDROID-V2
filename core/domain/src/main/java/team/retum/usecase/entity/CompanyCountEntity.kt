package team.retum.usecase.entity

import team.retum.network.model.response.FetchCompanyPageCountResponse

data class CompanyCountEntity(
    val totalPageCount: Long,
)

fun FetchCompanyPageCountResponse.toCompanyCountEntity() = CompanyCountEntity(
    totalPageCount = this.totalPageCount,
)