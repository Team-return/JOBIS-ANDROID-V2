package team.retum.usecase.entity

import team.retum.network.model.response.FetchReviewsResponse

data class FetchReviewsEntity(
    val reviews: List<Review>,
) {
    data class Review(
        val reviewId: String,
        val year: String,
        val writer: String,
        val date: String,
    )
}

internal fun FetchReviewsResponse.toEntity() = FetchReviewsEntity(
    reviews = this.reviews.map { it.toEntity() },
)

private fun FetchReviewsResponse.Review.toEntity() = FetchReviewsEntity.Review(
    reviewId = this.reviewId,
    year = this.year.toString(),
    writer = this.writer,
    date = this.date,
)
