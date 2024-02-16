package team.retum.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import team.retum.network.di.RequestUrls
import team.retum.network.model.response.CompaniesResponse
import team.retum.network.model.response.CompanyCountResponse

interface CompanyApi {
    @GET(RequestUrls.Companies.student)
    suspend fun fetchCompanies(
        @Query("page") page: Int,
        @Query("name") name: String?,
    ): CompaniesResponse

    @GET(RequestUrls.Companies.count)
    suspend fun fetchPageCount(
        @Query("name") name: String?,
    ): CompanyCountResponse
}