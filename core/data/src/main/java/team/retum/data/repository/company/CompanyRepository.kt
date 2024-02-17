package team.retum.data.repository.company

import team.retum.network.model.response.FetchCompaniesResponse
import team.retum.network.model.response.FetchCompanyPageCountResponse

interface CompanyRepository {
    suspend fun fetchCompanies(page: Int, name:String?): FetchCompaniesResponse

    suspend fun fetchPageCount(name: String?): FetchCompanyPageCountResponse
}