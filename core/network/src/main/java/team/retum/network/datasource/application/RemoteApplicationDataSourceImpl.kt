package team.retum.network.datasource.application

import team.retum.network.api.ApplicationApi
import team.retum.network.model.response.application.FetchAppliedCompaniesResponse
import team.retum.network.model.response.application.FetchEmploymentCountResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class RemoteApplicationDataSourceImpl @Inject constructor(
    private val applicationApi: ApplicationApi,
): RemoteApplicationDataSource {
    override suspend fun fetchAppliedCompanies(): FetchAppliedCompaniesResponse {
        return RequestHandler<FetchAppliedCompaniesResponse>().request {
            applicationApi.fetchAppliedCompanies()
        }
    }

    override suspend fun fetchEmploymentCount(): FetchEmploymentCountResponse {
        return RequestHandler<FetchEmploymentCountResponse>().request {
            applicationApi.fetchEmploymentCount()
        }
    }
}
