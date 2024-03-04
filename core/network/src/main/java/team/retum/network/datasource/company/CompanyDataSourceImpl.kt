package team.retum.network.datasource.company

import team.retum.network.api.CompanyApi
import team.retum.network.model.response.FetchCompaniesResponse
import team.retum.network.model.response.FetchCompanyPageCountResponse
import team.retum.network.model.response.company.FetchReviewableCompaniesResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class CompanyDataSourceImpl @Inject constructor(
    private val companyApi: CompanyApi,
) : CompanyDataSource {
    override suspend fun fetchCompanies(page: Int, name: String?): FetchCompaniesResponse =
        RequestHandler<FetchCompaniesResponse>().request {
            companyApi.fetchCompanies(page = page, name = name)
        }

    override suspend fun fetchPageCount(name: String?): FetchCompanyPageCountResponse =
        RequestHandler<FetchCompanyPageCountResponse>().request { companyApi.fetchPageCount(name = name) }

    override suspend fun fetchReviewableCompanies(): FetchReviewableCompaniesResponse {
        return RequestHandler<FetchReviewableCompaniesResponse>().request {
            companyApi.fetchReviewableCompanies()
        }
    }
}
