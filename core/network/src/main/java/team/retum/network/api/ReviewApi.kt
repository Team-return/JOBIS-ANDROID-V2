package team.retum.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.network.di.RequestUrls
import team.retum.network.model.request.PostReviewRequest
import team.retum.network.model.response.FetchQuestionsResponse
import team.retum.network.model.response.FetchReviewDetailResponse
import team.retum.network.model.response.FetchReviewsCountResponse
import team.retum.network.model.response.FetchReviewsResponse

interface ReviewApi {
    @POST(RequestUrls.Reviews.post)
    suspend fun postReview(
        @Body reviewRequest: PostReviewRequest,
    )

    @GET(RequestUrls.Reviews.reviews) // 컴퍼니쪽 후기 조회
    suspend fun fetchReviews(
        @Query("page") page: Int?,
        @Query("location") location: InterviewLocation?,
        @Query("interview_type") interviewType: InterviewType?,
        @Query("company_id") companyId: Long?,
        @Query("keyword") keyword: String?,
        @Query("year") year: Int?,
        @Query("job_code") jobCode: Long?,
    ): FetchReviewsResponse

    @GET(RequestUrls.Reviews.details) // 컴퍼니쪽 후기 상세 조회
    suspend fun fetchReviewDetail(
        @Path(RequestUrls.PATH.reviewId) reviewId: String,
    ): FetchReviewDetailResponse

    @GET(RequestUrls.Reviews.questions)
    suspend fun fetchQuestions(): FetchQuestionsResponse

    @GET(RequestUrls.Reviews.count)
    suspend fun fetchReviewsCount(): FetchReviewsCountResponse
}
