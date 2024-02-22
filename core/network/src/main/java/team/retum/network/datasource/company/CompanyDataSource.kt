package team.retum.network.datasource.company

import team.retum.network.model.response.FetchCompaniesResponse
import team.retum.network.model.response.FetchCompanyPageCountResponse

interface CompanyDataSource {
    suspend fun fetchCompanies(page: Int, name: String?): FetchCompaniesResponse

    suspend fun fetchPageCount(name: String?): FetchCompanyPageCountResponse
}