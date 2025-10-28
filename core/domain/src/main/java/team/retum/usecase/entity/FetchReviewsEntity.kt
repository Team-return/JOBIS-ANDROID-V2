package team.retum.usecase.entity

import androidx.compose.runtime.Immutable
import team.retum.common.utils.ResourceKeys
import team.retum.network.model.response.FetchReviewsResponse

data class FetchReviewsEntity(
    val reviews: List<Review>,
) {
    @Immutable
    data class Review(
        val reviewId: Long,
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

/**
 * Converts a FetchReviewsResponse.Review into a FetchReviewsEntity.Review.
 *
 * @return A FetchReviewsEntity.Review with response fields mapped directly, the `companyLogoUrl` prefixed by `ResourceKeys.IMAGE_URL`, and `year` converted to a string.
 */
private fun FetchReviewsResponse.Review.toEntity() = FetchReviewsEntity.Review(
    reviewId = this.reviewId,
    companyName = this.companyName,
    companyLogoUrl = ResourceKeys.IMAGE_URL + this.companyLogoUrl,
    major = this.major,
    year = this.year.toString(),
    writer = this.writer,
)