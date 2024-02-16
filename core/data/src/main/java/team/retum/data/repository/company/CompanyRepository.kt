package team.retum.data.repository.company

import team.retum.network.model.response.CompaniesResponse
import team.retum.network.model.response.CompanyCountResponse

interface CompanyRepository {
    suspend fun fetchCompanies(page: Int, name:String?): CompaniesResponse

    suspend fun fetchPageCount(name: String?): CompanyCountResponse
}