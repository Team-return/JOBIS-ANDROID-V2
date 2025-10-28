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
    /**
         * Submits a new review to the backend.
         *
         * @param postReviewRequest The review payload to submit.
         */
        override suspend fun postReview(postReviewRequest: PostReviewRequest) =
        RequestHandler<Unit>().request { reviewApi.postReview(postReviewRequest) }

    /**
         * Fetches a paginated list of reviews filtered by optional criteria.
         *
         * @param page Page number for pagination; when null, the API may use a default.
         * @param location Filter by interview location.
         * @param interviewType Filter by interview type.
         * @param keyword Full-text search keyword to filter reviews.
         * @param year Filter reviews by interview year.
         * @param companyId Filter reviews belonging to the specified company.
         * @param code Additional numeric filter used by the API (business-specific).
         * @return A FetchReviewsResponse containing the matched reviews and pagination metadata.
         */
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

    /**
         * Retrieves detailed information for a specific review.
         *
         * @param reviewId The identifier of the review to retrieve.
         * @return The fetched review detail response containing the review's full information.
         */
        override suspend fun fetchReviewDetail(reviewId: String): FetchReviewDetailResponse =
        RequestHandler<FetchReviewDetailResponse>().request { reviewApi.fetchReviewDetail(reviewId) }

    /**
         * Retrieves the list of predefined interview questions used in reviews.
         *
         * @return A FetchQuestionsResponse containing the available interview questions.
         */
        override suspend fun fetchQuestions(): FetchQuestionsResponse =
        RequestHandler<FetchQuestionsResponse>().request { reviewApi.fetchQuestions() }

    /**
         * Retrieves aggregated counts for reviews.
         *
         * @return The FetchReviewsCountResponse containing review count data.
         */
        override suspend fun fetchReviewsCount(): FetchReviewsCountResponse =
        RequestHandler<FetchReviewsCountResponse>().request { reviewApi.fetchReviewsCount() }

    /**
         * Retrieves the authenticated user's reviews.
         *
         * @return `FetchMyReviewResponse` containing the user's reviews and any associated metadata.
         */
        override suspend fun fetchMyReviews(): FetchMyReviewResponse =
        RequestHandler<FetchMyReviewResponse>().request { reviewApi.fetchMyReviews() }
}