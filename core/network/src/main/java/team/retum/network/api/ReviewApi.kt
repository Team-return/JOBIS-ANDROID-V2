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

    /**
     * Retrieves a paginated list of reviews filtered by the provided query parameters.
     *
     * @param page The page index to fetch.
     * @param location Filter by interview location.
     * @param interviewType Filter by interview type.
     * @param companyId Filter reviews for the specified company (mapped to query name `company_id`).
     * @param keyword Filter reviews containing the given keyword.
     * @param year Filter reviews by interview year.
     * @param code Filter reviews by specific review code.
     * @return A FetchReviewsResponse containing the requested page of reviews and related metadata.
     */
    @GET(RequestUrls.Reviews.reviews)
    suspend fun fetchReviews(
        @Query("page") page: Int?,
        @Query("location") location: InterviewLocation?,
        @Query("type") interviewType: InterviewType?,
        @Query("company_id") companyId: Long?,
        @Query("keyword") keyword: String?,
        @Query("year") year: Int?,
        @Query("code") code: Long?,
    ): FetchReviewsResponse

    /**
     * Retrieves detailed information for a specific review.
     *
     * @param reviewId The review's unique identifier used in the request path.
     * @return The fetched review details as a [FetchReviewDetailResponse].
     */
    @GET(RequestUrls.Reviews.details)
    suspend fun fetchReviewDetail(
        @Path(RequestUrls.PATH.reviewId) reviewId: String,
    ): FetchReviewDetailResponse

    /**
     * Fetches the available interview questions.
     *
     * @return A [FetchQuestionsResponse] containing the list of interview questions and any related metadata.
     */
    @GET(RequestUrls.Reviews.questions)
    suspend fun fetchQuestions(): FetchQuestionsResponse

    /**
     * Retrieves the total number of reviews.
     *
     * @return A [FetchReviewsCountResponse] containing the review count information.
     */
    @GET(RequestUrls.Reviews.count)
    suspend fun fetchReviewsCount(): FetchReviewsCountResponse

    /**
     * Retrieves reviews authored by the authenticated user.
     *
     * @return A FetchMyReviewResponse containing the user's reviews and any related metadata.
     */
    @GET(RequestUrls.Reviews.my)
    suspend fun fetchMyReviews(): FetchMyReviewResponse
}