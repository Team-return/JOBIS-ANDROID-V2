package team.retum.usecase.entity

import team.retum.network.model.response.FetchReviewsCountResponse

data class ReviewsCountEntity(
    val totalPageCount: Long,
)

internal fun FetchReviewsCountResponse.toEntity() = ReviewsCountEntity(
    totalPageCount = totalPageCount,
)
