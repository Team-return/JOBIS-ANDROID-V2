package team.retum.usecase.entity

import team.retum.common.utils.ResourceKeys
import team.retum.network.model.response.FetchCompaniesResponse

enum class HasRecruitment {
    TRUE,
    FALSE,
    LOADING,
}

data class CompaniesEntity(
    val companies: List<CompanyEntity>,
) {
    data class CompanyEntity(
        val id: Long,
        val name: String,
        val logoUrl: String,
        val take: Float,
        val hasRecruitment: HasRecruitment,
        val takeText: String,
    ) {
        companion object {
            fun getDefaultEntity() = CompanyEntity(
                id = 0,
                name = "",
                logoUrl = "",
                take = 0f,
                hasRecruitment = HasRecruitment.LOADING,
                takeText = "",
            )
        }
    }
}

internal fun FetchCompaniesResponse.toCompaniesEntity() = CompaniesEntity(
    companies = this.companies.map { it.toEntity() },
)

private fun FetchCompaniesResponse.CompanyResponse.toEntity() = CompaniesEntity.CompanyEntity(
    id = this.id,
    name = this.name,
    logoUrl = ResourceKeys.IMAGE_URL + this.logoUrl,
    take = this.take,
    hasRecruitment = when (this.hasRecruitment) {
        true -> HasRecruitment.TRUE
        false -> HasRecruitment.FALSE
    },
    takeText = if (take == 0f) {
        ""
    } else {
        "연매출 ${take}억"
    },
)
