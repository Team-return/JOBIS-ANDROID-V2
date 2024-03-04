package team.retum.data.repository

import team.retum.network.datasource.ReviewDataSource
import team.retum.network.model.request.PostReviewRequest
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewDataSource: ReviewDataSource,
) : ReviewRepository {
    override suspend fun postReview(reviewRequest: PostReviewRequest) =
        reviewDataSource.postReview(reviewRequest)

}