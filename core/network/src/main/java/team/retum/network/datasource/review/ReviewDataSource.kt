package team.retum.network.datasource.review

import team.retum.network.model.request.PostReviewRequest
import team.retum.network.model.response.FetchReviewDetailResponse
import team.retum.network.model.response.FetchReviewsResponse

interface ReviewDataSource {
    suspend fun postReview(postReviewRequest: PostReviewRequest)

    suspend fun fetchReviews(companyId: Long): FetchReviewsResponse

    suspend fun fetchReviewDetail(reviewId: String): FetchReviewDetailResponse
}
