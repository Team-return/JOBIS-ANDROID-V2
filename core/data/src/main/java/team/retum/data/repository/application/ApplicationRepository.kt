package team.retum.data.repository.application

import team.retum.network.model.response.application.FetchAppliedCompaniesResponse
import team.retum.network.model.response.application.FetchEmploymentCountResponse

interface ApplicationRepository {
    suspend fun fetchAppliedCompanies(): FetchAppliedCompaniesResponse

    suspend fun fetchEmploymentCount(): FetchEmploymentCountResponse
}
