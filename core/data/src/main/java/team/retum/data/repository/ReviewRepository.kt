package team.retum.data.repository

import team.retum.network.model.request.PostReviewRequest

interface ReviewRepository {
    suspend fun postReview(reviewRequest: PostReviewRequest)
}