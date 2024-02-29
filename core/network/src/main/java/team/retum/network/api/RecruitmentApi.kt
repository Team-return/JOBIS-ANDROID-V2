package team.retum.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import team.retum.network.di.RequestUrls
import team.retum.network.model.response.FetchRecruitmentPageCountResponse
import team.retum.network.model.response.FetchRecruitmentsResponse

interface RecruitmentApi {

    @GET(RequestUrls.Recruitments.student)
    suspend fun fetchRecruitments(
        @Query("page") page: Int?,
        @Query("name") name: String?,
        @Query("job_code") jobCode: Long?,
        @Query("tech_code") techCode: String?,
        @Query("winter_intern") winterIntern: Boolean,
    ): FetchRecruitmentsResponse

    @GET(RequestUrls.Recruitments.count)
    suspend fun fetchRecruitmentPageCount(
        @Query("name") name: String?,
        @Query("job_code") jobCode: Long?,
        @Query("tech_code") techCode: String?,
        @Query("winter_intern") winterIntern: Boolean,
    ): FetchRecruitmentPageCountResponse
}
