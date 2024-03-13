package team.retum.data.repository.review

import team.retum.network.model.request.PostReviewRequest
import team.retum.network.model.response.FetchReviewDetailResponse
import team.retum.network.model.response.FetchReviewsResponse

interface ReviewRepository {
    suspend fun postReview(reviewRequest: PostReviewRequest)

    suspend fun fetchReviews(companyId: Long): FetchReviewsResponse

    suspend fun fetchReviewDetail(reviewId: String): FetchReviewDetailResponse
}
