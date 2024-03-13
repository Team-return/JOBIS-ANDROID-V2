package team.retum.usecase.entity

import team.retum.network.model.response.FetchCompaniesResponse

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

internal fun FetchCompaniesResponse.toCompaniesEntity() = CompaniesEntity(
    companies = this.companies.map { it.toEntity() }
)

private fun FetchCompaniesResponse.CompanyResponse.toEntity() = CompaniesEntity.CompanyEntity(
    id = this.id,
    name = this.name,
    logoUrl = this.logoUrl,
    take = this.take,
    hasRecruitment = this.hasRecruitment,
)