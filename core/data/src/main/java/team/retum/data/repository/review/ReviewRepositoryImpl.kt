package team.retum.data.repository.review

import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.network.datasource.review.ReviewDataSource
import team.retum.network.model.request.PostReviewRequest
import team.retum.network.model.response.FetchMyReviewResponse
import team.retum.network.model.response.FetchQuestionsResponse
import team.retum.network.model.response.FetchReviewDetailResponse
import team.retum.network.model.response.FetchReviewsCountResponse
import team.retum.network.model.response.FetchReviewsResponse
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewDataSource: ReviewDataSource,
) : ReviewRepository {
    override suspend fun postReview(reviewRequest: PostReviewRequest) =
        reviewDataSource.postReview(reviewRequest)

    override suspend fun fetchReviews(
        page: Int?,
        location: InterviewLocation?,
        interviewType: InterviewType?,
        keyword: String?,
        year: Int?,
        companyId: Long?,
        code: Long?,
    ): FetchReviewsResponse =
        reviewDataSource.fetchReviews(
            page = page,
            location = location,
            interviewType = interviewType,
            keyword = keyword,
            year = year,
            companyId = companyId,
            code = code,
        )

    override suspend fun fetchReviewDetail(reviewId: String): FetchReviewDetailResponse =
        reviewDataSource.fetchReviewDetail(reviewId)

    override suspend fun fetchQuestions(): FetchQuestionsResponse =
        reviewDataSource.fetchQuestions()

    override suspend fun fetchReviewsCount(): FetchReviewsCountResponse =
        reviewDataSource.fetchReviewsCount()

    override suspend fun fetchMyReviews(): FetchMyReviewResponse =
        reviewDataSource.fetchMyReviews()
}
