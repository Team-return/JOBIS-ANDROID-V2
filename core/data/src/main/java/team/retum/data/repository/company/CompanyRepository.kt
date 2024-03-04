package team.retum.data.repository.company

import team.retum.network.model.response.FetchCompaniesResponse
import team.retum.network.model.response.FetchCompanyPageCountResponse
import team.retum.network.model.response.company.FetchReviewableCompaniesResponse

interface CompanyRepository {
    suspend fun fetchCompanies(page: Int, name:String?): FetchCompaniesResponse
    suspend fun fetchPageCount(name: String?): FetchCompanyPageCountResponse
    suspend fun fetchReviewableCompanies(): FetchReviewableCompaniesResponse
}
