package team.retum.data.repository.review

import team.retum.network.datasource.review.ReviewDataSource
import team.retum.network.model.request.PostReviewRequest
import team.retum.network.model.response.FetchReviewDetailResponse
import team.retum.network.model.response.FetchReviewsResponse
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewDataSource: ReviewDataSource,
) : ReviewRepository {
    override suspend fun postReview(reviewRequest: PostReviewRequest) =
        reviewDataSource.postReview(reviewRequest)

    override suspend fun fetchReviews(companyId: Long): FetchReviewsResponse =
        reviewDataSource.fetchReviews(companyId)

    override suspend fun fetchReviewDetail(reviewId: String): FetchReviewDetailResponse =
        reviewDataSource.fetchReviewDetail(reviewId)

}
