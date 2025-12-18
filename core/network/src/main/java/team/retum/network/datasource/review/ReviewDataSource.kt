package team.retum.network.datasource.review

import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.network.model.request.PostReviewRequest
import team.retum.network.model.response.FetchMyReviewResponse
import team.retum.network.model.response.FetchQuestionsResponse
import team.retum.network.model.response.FetchReviewDetailResponse
import team.retum.network.model.response.FetchReviewsCountResponse
import team.retum.network.model.response.FetchReviewsResponse

interface ReviewDataSource {
    suspend fun postReview(postReviewRequest: PostReviewRequest)

    suspend fun fetchReviews(
        page: Int?,
        location: InterviewLocation?,
        interviewType: InterviewType?,
        companyId: Long?,
        keyword: String?,
        year: List<Int>?,
        code: Long?,
    ): FetchReviewsResponse

    suspend fun fetchReviewDetail(reviewId: Long): FetchReviewDetailResponse

    suspend fun fetchQuestions(): FetchQuestionsResponse

    suspend fun fetchReviewsCount(
        location: InterviewLocation?,
        interviewType: InterviewType?,
        keyword: String?,
        year: List<Int>?,
        code: Long?,
    ): FetchReviewsCountResponse

    suspend fun fetchMyReviews(): FetchMyReviewResponse
}
