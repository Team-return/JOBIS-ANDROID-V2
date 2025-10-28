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
    /**
 * Submits a new review to the server.
 *
 * @param postReviewRequest The payload containing the review details to create.
 */
suspend fun postReview(postReviewRequest: PostReviewRequest)

    /**
     * Retrieve a paginated list of reviews filtered by the provided optional criteria.
     *
     * @param page Page number for pagination; when null, the default page is used.
     * @param location Filter reviews by interview location.
     * @param interviewType Filter reviews by interview type.
     * @param keyword Search term to match against review content or metadata.
     * @param year Filter reviews by the interview year.
     * @param companyId Filter reviews for a specific company.
     * @param code Filter reviews by a specific code or tag.
     * @return A FetchReviewsResponse containing the matching reviews and pagination metadata.
     */
    suspend fun fetchReviews(
        page: Int?,
        location: InterviewLocation?,
        interviewType: InterviewType?,
        keyword: String?,
        year: Int?,
        companyId: Long?,
        code: Long?,
    ): FetchReviewsResponse

    /**
 * Retrieve detailed information for a review identified by its ID.
 *
 * @return `FetchReviewDetailResponse` containing detailed data for the specified review.
 */
suspend fun fetchReviewDetail(reviewId: String): FetchReviewDetailResponse

    /**
 * Retrieves the list of predefined interview questions used when creating or browsing reviews.
 *
 * @return A FetchQuestionsResponse containing the available questions.
 */
suspend fun fetchQuestions(): FetchQuestionsResponse

    /**
 * Retrieves aggregated counts of reviews, such as totals by category or status.
 *
 * @return A FetchReviewsCountResponse containing the review count summary.
 */
suspend fun fetchReviewsCount(): FetchReviewsCountResponse

    /**
 * Retrieves reviews created by the current user.
 *
 * @return A FetchMyReviewResponse containing the current user's reviews.
 */
suspend fun fetchMyReviews(): FetchMyReviewResponse
}