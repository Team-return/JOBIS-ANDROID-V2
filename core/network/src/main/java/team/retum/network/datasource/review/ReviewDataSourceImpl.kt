package team.retum.network.datasource.review

import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.network.api.ReviewApi
import team.retum.network.model.request.PostReviewRequest
import team.retum.network.model.response.FetchMyReviewResponse
import team.retum.network.model.response.FetchQuestionsResponse
import team.retum.network.model.response.FetchReviewDetailResponse
import team.retum.network.model.response.FetchReviewsCountResponse
import team.retum.network.model.response.FetchReviewsResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class ReviewDataSourceImpl @Inject constructor(
    private val reviewApi: ReviewApi,
) : ReviewDataSource {
    override suspend fun postReview(postReviewRequest: PostReviewRequest) =
        RequestHandler<Unit>().request { reviewApi.postReview(postReviewRequest) }

    override suspend fun fetchReviews(
        page: Int?,
        location: InterviewLocation?,
        interviewType: InterviewType?,
        keyword: String?,
        year: Int?,
        companyId: Long?,
        code: Long?,
    ): FetchReviewsResponse =
        RequestHandler<FetchReviewsResponse>().request {
            reviewApi.fetchReviews(
                page = page,
                location = location,
                interviewType = interviewType,
                companyId = companyId,
                keyword = keyword,
                year = year,
                code = code,
            )
        }

    override suspend fun fetchReviewDetail(reviewId: String): FetchReviewDetailResponse =
        RequestHandler<FetchReviewDetailResponse>().request { reviewApi.fetchReviewDetail(reviewId) }

    override suspend fun fetchQuestions(): FetchQuestionsResponse =
        RequestHandler<FetchQuestionsResponse>().request { reviewApi.fetchQuestions() }

    override suspend fun fetchReviewsCount(): FetchReviewsCountResponse =
        RequestHandler<FetchReviewsCountResponse>().request { reviewApi.fetchReviewsCount() }

    override suspend fun fetchMyReviews(): FetchMyReviewResponse =
        RequestHandler<FetchMyReviewResponse>().request { reviewApi.fetchMyReviews() }
}
