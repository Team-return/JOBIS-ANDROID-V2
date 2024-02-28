package team.retum.network.api

import retrofit2.http.GET
import team.retum.network.di.RequestUrls
import team.retum.network.model.response.application.FetchAppliedCompaniesResponse
import team.retum.network.model.response.application.FetchEmploymentCountResponse

interface ApplicationApi {
    @GET(RequestUrls.Applications.student)
    suspend fun fetchAppliedCompanies(): FetchAppliedCompaniesResponse

    @GET(RequestUrls.Applications.employmentCount)
    suspend fun fetchEmploymentCount(): FetchEmploymentCountResponse
}
