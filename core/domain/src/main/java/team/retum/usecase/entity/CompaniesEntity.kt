package team.retum.usecase.entity

import team.retum.network.model.response.CompaniesResponse

data class CompaniesEntity(
    val companies: List<CompanyEntity>,
) {
    data class CompanyEntity(
        val id: Long,
        val name: String,
        val logoUrl: String,
        val take: Float,
        val hasRecruitment: Boolean,
    )
}

fun CompaniesResponse.toCompaniesEntity() = CompaniesEntity(
    companies = this.companies.map { it.toEntity() }
)

private fun CompaniesResponse.CompanyResponse.toEntity() = CompaniesEntity.CompanyEntity(
    id = id,
    name = name,
    logoUrl = logoUrl,
    take = take,
    hasRecruitment = hasRecruitment,
)