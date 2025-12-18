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
import team.retum.network.model.response.FetchMyReviewResponse
import team.retum.network.model.response.FetchQuestionsResponse
import team.retum.network.model.response.FetchReviewDetailResponse
import team.retum.network.model.response.FetchReviewsCountResponse
import team.retum.network.model.response.FetchReviewsResponse

interface ReviewApi {
    @POST(RequestUrls.Reviews.post)
    suspend fun postReview(
        @Body reviewRequest: PostReviewRequest,
    )

    @GET(RequestUrls.Reviews.reviews)
    suspend fun fetchReviews(
        @Query("page") page: Int?,
        @Query("location") location: InterviewLocation?,
        @Query("type") interviewType: InterviewType?,
        @Query("company_id") companyId: Long?,
        @Query("keyword") keyword: String?,
        @Query("years") year: List<Int>?,
        @Query("code") code: Long?,
    ): FetchReviewsResponse

    @GET(RequestUrls.Reviews.details)
    suspend fun fetchReviewDetail(
        @Path(RequestUrls.PATH.reviewId) reviewId: Long,
    ): FetchReviewDetailResponse

    @GET(RequestUrls.Reviews.questions)
    suspend fun fetchQuestions(): FetchQuestionsResponse

    @GET(RequestUrls.Reviews.count)
    suspend fun fetchReviewsCount(
        @Query("location") location: InterviewLocation?,
        @Query("type") interviewType: InterviewType?,
        @Query("keyword") keyword: String?,
        @Query("years") year: List<Int>?,
        @Query("code") code: Long?,
    ): FetchReviewsCountResponse

    @GET(RequestUrls.Reviews.my)
    suspend fun fetchMyReviews(): FetchMyReviewResponse
}
