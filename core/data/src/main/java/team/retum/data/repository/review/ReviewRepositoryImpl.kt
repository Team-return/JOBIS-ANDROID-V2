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
    /**
         * Submits a review to the data source.
         *
         * @param reviewRequest The review payload to submit.
         * @return The response returned by the data source after posting the review.
         */
        override suspend fun postReview(reviewRequest: PostReviewRequest) =
        reviewDataSource.postReview(reviewRequest)

    /**
         * Fetches a paginated list of reviews matching the provided filters.
         *
         * @param page The page number to retrieve, or `null` to use the default first page.
         * @param location Filter by interview location, or `null` to include all locations.
         * @param interviewType Filter by interview type, or `null` to include all types.
         * @param keyword Filter reviews by keyword contained in their content, or `null` for no keyword filtering.
         * @param year Filter reviews by the interview year, or `null` to include all years.
         * @param companyId Filter reviews for a specific company ID, or `null` to include all companies.
         * @param code Filter reviews by a specific code, or `null` to include all codes.
         * @return A FetchReviewsResponse containing the matching reviews and pagination metadata.
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
        reviewDataSource.fetchReviews(
            page = page,
            location = location,
            interviewType = interviewType,
            keyword = keyword,
            year = year,
            companyId = companyId,
            code = code,
        )

    /**
         * Retrieves detailed information for a specific review.
         *
         * @param reviewId The identifier of the review to retrieve.
         * @return A FetchReviewDetailResponse containing the review's detailed data.
         */
        override suspend fun fetchReviewDetail(reviewId: String): FetchReviewDetailResponse =
        reviewDataSource.fetchReviewDetail(reviewId)

    /**
         * Retrieves the predefined review questions used for submitting or displaying reviews.
         *
         * @return A [FetchQuestionsResponse] containing the list of questions and any related metadata.
         */
        override suspend fun fetchQuestions(): FetchQuestionsResponse =
        reviewDataSource.fetchQuestions()

    /**
         * Retrieves a summary of review counts.
         *
         * @return A FetchReviewsCountResponse containing the total number of reviews and any related count breakdowns.
         */
        override suspend fun fetchReviewsCount(): FetchReviewsCountResponse =
        reviewDataSource.fetchReviewsCount()

    /**
         * Retrieves the authenticated user's submitted reviews.
         *
         * @return A [FetchMyReviewResponse] containing the user's reviews and any associated metadata.
         */
        override suspend fun fetchMyReviews(): FetchMyReviewResponse =
        reviewDataSource.fetchMyReviews()
}