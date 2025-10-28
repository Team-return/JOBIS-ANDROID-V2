package team.retum.usecase.entity

import team.retum.network.model.response.FetchReviewsCountResponse

data class ReviewsCountEntity(
    val totalPageCount: Long,
)

/**
 * Converts this FetchReviewsCountResponse into a ReviewsCountEntity.
 *
 * @return A ReviewsCountEntity whose `totalPageCount` is copied from this response.
 */
internal fun FetchReviewsCountResponse.toEntity() = ReviewsCountEntity(
    totalPageCount = totalPageCount,
)