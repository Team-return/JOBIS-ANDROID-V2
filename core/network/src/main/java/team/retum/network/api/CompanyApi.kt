package team.retum.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import team.retum.network.di.RequestUrls
import team.retum.network.model.response.FetchCompaniesResponse
import team.retum.network.model.response.FetchCompanyPageCountResponse

interface CompanyApi {
    @GET(RequestUrls.Companies.student)
    suspend fun fetchCompanies(
        @Query("page") page: Int,
        @Query("name") name: String?,
    ): FetchCompaniesResponse

    @GET(RequestUrls.Companies.count)
    suspend fun fetchPageCount(
        @Query("name") name: String?,
    ): FetchCompanyPageCountResponse
}