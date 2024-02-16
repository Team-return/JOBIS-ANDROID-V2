package team.retum.network.datasource.company

import team.retum.network.api.CompanyApi
import team.retum.network.model.response.CompaniesResponse
import team.retum.network.model.response.CompanyCountResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class CompanyDataSourceImpl @Inject constructor(
    private val companyApi: CompanyApi,
) : CompanyDataSource {
    override suspend fun fetchCompanies(page: Int, name: String?): CompaniesResponse =
        RequestHandler<CompaniesResponse>().request {
            companyApi.fetchCompanies(page = page, name = name)
        }

    override suspend fun fetchPageCount(name: String?): CompanyCountResponse =
        RequestHandler<CompanyCountResponse>().request {
            companyApi.fetchPageCount(name = name)
        }

}