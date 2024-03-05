package team.retum.usecase.usecase.review

import team.retum.data.repository.review.ReviewRepository
import team.retum.usecase.entity.toEntity
import javax.inject.Inject

class FetchReviewDetailUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {

    suspend operator fun invoke(reviewId: String) = runCatching {
        reviewRepository.fetchReviewDetail(reviewId).toEntity()
    }
}
