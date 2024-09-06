package team.retum.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import team.retum.network.di.RequestUrls
import team.retum.network.model.request.PostReviewRequest
import team.retum.network.model.response.FetchReviewDetailResponse
import team.retum.network.model.response.FetchReviewsResponse

interface ReviewApi {
    @POST(RequestUrls.Reviews.post)
    suspend fun postReview(
        @Body reviewRequest: PostReviewRequest,
    )

    @GET(RequestUrls.Reviews.reviews)
    suspend fun fetchReviews(
        @Path(RequestUrls.PATH.companyId) companyId: Long,
    ): FetchReviewsResponse

    @GET(RequestUrls.Reviews.details)
    suspend fun fetchReviewDetail(
        @Path(RequestUrls.PATH.reviewId) reviewId: String,
    ): FetchReviewDetailResponse
}
