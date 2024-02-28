package team.retum.network.datasource.application

import team.retum.network.model.response.application.FetchAppliedCompaniesResponse

interface RemoteApplicationDataSource {
    suspend fun fetchAppliedCompanies(): FetchAppliedCompaniesResponse
}
