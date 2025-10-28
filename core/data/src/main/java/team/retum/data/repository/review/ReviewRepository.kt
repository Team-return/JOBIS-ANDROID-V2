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
    /**
 * Persists a new review using the provided review request data.
 *
 * @param reviewRequest The request payload containing the review details to save.
 */
suspend fun postReview(reviewRequest: PostReviewRequest)

    /**
     * Retrieves a page of reviews applying optional filters.
     *
     * @param page The page number to retrieve; null to use default paging.
     * @param location Filter by interview location; null to include all locations.
     * @param interviewType Filter by interview type; null to include all types.
     * @param keyword Full- or partial-text search term; null to disable keyword filtering.
     * @param year Filter reviews by year; null to include all years.
     * @param companyId Filter reviews for a specific company id; null to include all companies.
     * @param code Additional numeric filter for review classification; null to ignore.
     * @return A FetchReviewsResponse containing the matching reviews and related pagination metadata.
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
 * Retrieves detailed information for a specific review.
 *
 * @param reviewId The unique identifier of the review to retrieve.
 * @return A FetchReviewDetailResponse containing the review's full details.
 */
suspend fun fetchReviewDetail(reviewId: String): FetchReviewDetailResponse

    /**
 * Retrieves the list of interview questions associated with reviews.
 *
 * @return A FetchQuestionsResponse containing the available questions and related metadata.
 */
suspend fun fetchQuestions(): FetchQuestionsResponse

    /**
 * Retrieves aggregate counts for reviews.
 *
 * @return FetchReviewsCountResponse containing the total number of reviews and any available breakdowns by criteria.
 */
suspend fun fetchReviewsCount(): FetchReviewsCountResponse

    /**
 * Retrieves reviews authored by the current authenticated user.
 *
 * @return `FetchMyReviewResponse` containing the current user's reviews and associated response metadata.
 */
suspend fun fetchMyReviews(): FetchMyReviewResponse
}