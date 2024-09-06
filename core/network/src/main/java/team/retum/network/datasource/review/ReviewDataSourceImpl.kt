package team.retum.network.datasource.review

import team.retum.network.api.ReviewApi
import team.retum.network.model.request.PostReviewRequest
import team.retum.network.model.response.FetchReviewDetailResponse
import team.retum.network.model.response.FetchReviewsResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class ReviewDataSourceImpl @Inject constructor(
    private val reviewApi: ReviewApi,
) : ReviewDataSource {
    override suspend fun postReview(postReviewRequest: PostReviewRequest) =
        RequestHandler<Unit>().request { reviewApi.postReview(postReviewRequest) }

    override suspend fun fetchReviews(companyId: Long): FetchReviewsResponse =
        RequestHandler<FetchReviewsResponse>().request { reviewApi.fetchReviews(companyId) }

    override suspend fun fetchReviewDetail(reviewId: String): FetchReviewDetailResponse =
        RequestHandler<FetchReviewDetailResponse>().request { reviewApi.fetchReviewDetail(reviewId) }
}
