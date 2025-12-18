package team.retum.usecase.usecase.review

import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.data.repository.review.ReviewRepository
import team.retum.usecase.entity.toEntity
import javax.inject.Inject

class FetchReviewsCountUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(
        location: InterviewLocation?,
        interviewType: InterviewType?,
        keyword: String?,
        year: List<Int>?,
        code: Long?,
    ) = runCatching {
        reviewRepository.fetchReviewsCount(
            location = location,
            interviewType = interviewType,
            keyword = keyword,
            year = year,
            code = code,
        ).toEntity()
    }
}
