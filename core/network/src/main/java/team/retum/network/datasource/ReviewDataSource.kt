package team.retum.network.datasource

import team.retum.network.model.request.PostReviewRequest

interface ReviewDataSource {
    suspend fun postReview(postReviewRequest: PostReviewRequest)
}