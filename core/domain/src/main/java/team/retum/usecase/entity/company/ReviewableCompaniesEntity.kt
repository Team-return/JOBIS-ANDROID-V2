package team.retum.usecase.entity.company

import team.retum.network.model.response.company.FetchReviewableCompaniesResponse

data class ReviewableCompaniesEntity(
    val companies: List<ReviewableCompanyEntity>,
) {
    data class ReviewableCompanyEntity(
        val id: Long,
        val name: String,
    )
}

internal fun FetchReviewableCompaniesResponse.toEntity() = ReviewableCompaniesEntity(
    companies = this.companies.map { it.toEntity() },
)

private fun FetchReviewableCompaniesResponse.CompanyResponse.toEntity() =
    ReviewableCompaniesEntity.ReviewableCompanyEntity(
        id = this.id,
        name = this.name.take(10) + "...",
    )
