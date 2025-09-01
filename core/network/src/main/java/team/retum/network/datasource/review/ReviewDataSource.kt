package team.retum.network.datasource.review

import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.network.model.request.PostReviewRequest
import team.retum.network.model.response.FetchReviewDetailResponse
import team.retum.network.model.response.FetchReviewsResponse

interface ReviewDataSource {
    suspend fun postReview(postReviewRequest: PostReviewRequest)

    suspend fun fetchReviews(
        page: Int?,
        location: InterviewLocation?,
        interviewType: InterviewType?,
        keyword: String?,
        year: Int?,
        companyId: Long?,
        jobCode: Long?,
    ): FetchReviewsResponse

    suspend fun fetchReviewDetail(reviewId: String): FetchReviewDetailResponse
}
