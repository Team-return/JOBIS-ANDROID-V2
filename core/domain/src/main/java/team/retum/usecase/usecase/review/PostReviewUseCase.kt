package team.retum.usecase.usecase.review

import team.retum.data.repository.review.ReviewRepository
import team.retum.usecase.entity.PostReviewEntity
import team.retum.usecase.entity.toPostReviewRequest
import javax.inject.Inject

class PostReviewUseCase @Inject constructor(
    private val postReviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(postReviewRequest: PostReviewEntity) = runCatching {
        postReviewRepository.postReview(postReviewRequest.toPostReviewRequest())
    }
}
