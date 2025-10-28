package team.retum.usecase.usecase.review

import team.retum.data.repository.review.ReviewRepository
import team.retum.usecase.entity.toEntity
import javax.inject.Inject

class FetchQuestionsUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    /**
     * Fetches review questions and converts them to domain entities.
     *
     * @return A `Result` containing the fetched questions as domain entities on success, or a failure with the caught exception.
     */
    suspend operator fun invoke() = runCatching {
        reviewRepository.fetchQuestions().toEntity()
    }
}