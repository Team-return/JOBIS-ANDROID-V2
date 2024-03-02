package team.retum.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import team.retum.network.di.RequestUrls
import team.retum.network.model.response.application.FetchAppliedCompaniesResponse
import team.retum.network.model.response.application.FetchEmploymentCountResponse
import team.retum.network.model.response.application.FetchRejectionReasonResponse

interface ApplicationApi {
    @GET(RequestUrls.Applications.student)
    suspend fun fetchAppliedCompanies(): FetchAppliedCompaniesResponse

    @GET(RequestUrls.Applications.employmentCount)
    suspend fun fetchEmploymentCount(): FetchEmploymentCountResponse

    @GET(RequestUrls.Applications.rejection)
    suspend fun fetchRejectionReason(
        @Path(RequestUrls.PATH.applicationId) applicationId: Long,
    ): FetchRejectionReasonResponse
}
