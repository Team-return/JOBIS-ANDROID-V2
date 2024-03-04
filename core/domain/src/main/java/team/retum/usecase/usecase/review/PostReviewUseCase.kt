package team.retum.usecase.usecase.review

import team.retum.data.repository.ReviewRepository
import team.retum.network.model.request.PostReviewRequest
import javax.inject.Inject

class PostReviewUseCase @Inject constructor(
    private val postReviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(postReviewRequest: PostReviewRequest) = runCatching {
        postReviewRepository.postReview(postReviewRequest)
    }
}