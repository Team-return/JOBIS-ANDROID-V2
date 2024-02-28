package team.retum.data.repository.application

import team.retum.network.model.response.application.FetchAppliedCompaniesResponse

interface ApplicationRepository {
    suspend fun fetchAppliedCompanies(): FetchAppliedCompaniesResponse
}
