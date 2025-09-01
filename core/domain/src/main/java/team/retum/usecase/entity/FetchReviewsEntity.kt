package team.retum.usecase.entity

import androidx.compose.runtime.Immutable
import team.retum.network.model.response.FetchReviewsResponse

data class FetchReviewsEntity(
    val reviews: List<Review>,
) {
    @Immutable
    data class Review(
        val reviewId: String,
        val companyName: String,
        val companyLogoUrl: String,
        val year: String,
        val writer: String,
        val major: String,
    )
}

internal fun FetchReviewsResponse.toEntity() = FetchReviewsEntity(
    reviews = this.reviews.map { it.toEntity() },
)

private fun FetchReviewsResponse.Review.toEntity() = FetchReviewsEntity.Review(
    reviewId = this.reviewId,
    companyName = this.companyName,
    companyLogoUrl = this.companyLogoUrl,
    major = this.major,
    year = this.year.toString(),
    writer = "${this.writer}님의 후기",
)
