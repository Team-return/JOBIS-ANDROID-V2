package team.retum.network.datasource.application

import team.retum.network.model.response.application.FetchAppliedCompaniesResponse
import team.retum.network.model.response.application.FetchEmploymentCountResponse

interface RemoteApplicationDataSource {
    suspend fun fetchAppliedCompanies(): FetchAppliedCompaniesResponse

    suspend fun fetchEmploymentCount(): FetchEmploymentCountResponse
}
