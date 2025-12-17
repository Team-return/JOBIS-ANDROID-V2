package team.retum.usecase.usecase.review

import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.data.repository.review.ReviewRepository
import team.retum.usecase.entity.toEntity
import javax.inject.Inject

class FetchReviewsUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(page: Int?, location: InterviewLocation?, interviewType: InterviewType?, keyword: String?, year: List<Int>?, companyId: Long?, code: Long?) = runCatching {
        reviewRepository.fetchReviews(page, location, interviewType, companyId, keyword, year, code).toEntity()
    }
}
