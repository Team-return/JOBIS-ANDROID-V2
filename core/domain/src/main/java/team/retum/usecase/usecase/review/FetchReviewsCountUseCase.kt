package team.retum.usecase.usecase.review

import team.retum.data.repository.review.ReviewRepository
import team.retum.usecase.entity.toEntity
import javax.inject.Inject

class FetchReviewsCountUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    /**
     * Fetches the total reviews count and maps it to its domain entity.
     *
     * @return A [Result] containing the reviews count entity on success, or the captured exception on failure.
     */
    suspend operator fun invoke() = runCatching {
        reviewRepository.fetchReviewsCount().toEntity()
    }
}