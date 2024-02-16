package team.retum.network.datasource.company

import team.retum.network.model.response.CompaniesResponse
import team.retum.network.model.response.CompanyCountResponse

interface CompanyDataSource {
    suspend fun fetchCompanies(page: Int, name: String?): CompaniesResponse

    suspend fun fetchPageCount(name: String?): CompanyCountResponse
}