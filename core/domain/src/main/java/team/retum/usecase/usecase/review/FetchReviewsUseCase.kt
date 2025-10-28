package team.retum.usecase.usecase.review

import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.data.repository.review.ReviewRepository
import team.retum.usecase.entity.toEntity
import javax.inject.Inject

class FetchReviewsUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    /**
     * Fetches reviews using the provided filter parameters and converts them to entities.
     *
     * @param page The page number for paginated results, or `null` to use default paging.
     * @param location Filter by interview location, or `null` to omit this filter.
     * @param interviewType Filter by interview type, or `null` to omit this filter.
     * @param keyword Filter reviews by a search keyword, or `null` to omit this filter.
     * @param year Filter by interview year, or `null` to omit this filter.
     * @param companyId Filter by company identifier, or `null` to omit this filter.
     * @param code Additional code-based filter, or `null` to omit this filter.
     * @return A `Result` containing the fetched reviews converted to entity form if successful, or the caught exception on failure.
     */
    suspend operator fun invoke(page: Int?, location: InterviewLocation?, interviewType: InterviewType?, keyword: String?, year: Int?, companyId: Long?, code: Long?) = runCatching {
        reviewRepository.fetchReviews(page, location, interviewType, keyword, year, companyId, code).toEntity()
    }
}