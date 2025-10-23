package team.retum.data.repository.review

import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.network.model.request.PostReviewRequest
import team.retum.network.model.response.FetchMyReviewResponse
import team.retum.network.model.response.FetchQuestionsResponse
import team.retum.network.model.response.FetchReviewDetailResponse
import team.retum.network.model.response.FetchReviewsCountResponse
import team.retum.network.model.response.FetchReviewsResponse

interface ReviewRepository {
    suspend fun postReview(reviewRequest: PostReviewRequest)

    suspend fun fetchReviews(
        page: Int?,
        location: InterviewLocation?,
        interviewType: InterviewType?,
        keyword: String?,
        year: Int?,
        companyId: Long?,
        code: Long?,
    ): FetchReviewsResponse

    suspend fun fetchReviewDetail(reviewId: String): FetchReviewDetailResponse

    suspend fun fetchQuestions(): FetchQuestionsResponse

    suspend fun fetchReviewsCount(): FetchReviewsCountResponse

    suspend fun fetchMyReviews(): FetchMyReviewResponse
}
