package team.retum.usecase.usecase.review

import team.retum.data.repository.review.ReviewRepository
import team.retum.usecase.entity.toEntity
import javax.inject.Inject

class FetchMyReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    /**
     * Fetches the current user's reviews and converts them to domain entities.
     *
     * @return A Result containing the fetched reviews converted to domain/entity objects on success, or an exception on failure.
     */
    suspend operator fun invoke() = runCatching {
        reviewRepository.fetchMyReviews().toEntity()
    }
}