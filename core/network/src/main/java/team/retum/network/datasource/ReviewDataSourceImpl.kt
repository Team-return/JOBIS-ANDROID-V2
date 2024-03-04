package team.retum.network.datasource

import team.retum.network.api.ReviewApi
import team.retum.network.model.request.PostReviewRequest
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class ReviewDataSourceImpl @Inject constructor(
    private val reviewApi: ReviewApi,
) : ReviewDataSource {
    override suspend fun postReview(postReviewRequest: PostReviewRequest) =
        RequestHandler<Unit>().request { reviewApi.postReview(postReviewRequest) }

}