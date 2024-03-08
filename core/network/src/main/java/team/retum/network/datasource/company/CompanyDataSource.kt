package team.retum.network.datasource.company

import team.retum.network.model.response.FetchCompaniesResponse
import team.retum.network.model.response.FetchCompanyPageCountResponse
import team.retum.network.model.response.company.FetchReviewableCompaniesResponse
import team.retum.network.model.response.company.FetchCompanyDetailsResponse

interface CompanyDataSource {
    suspend fun fetchCompanies(page: Int, name: String?): FetchCompaniesResponse
    suspend fun fetchPageCount(name: String?): FetchCompanyPageCountResponse
    suspend fun fetchReviewableCompanies(): FetchReviewableCompaniesResponse
    suspend fun fetchCompanyDetails(companyId: Long): FetchCompanyDetailsResponse
}
