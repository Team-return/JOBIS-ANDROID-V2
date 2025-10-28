package team.retum.usecase.entity

import team.retum.network.model.response.FetchMyReviewResponse
import javax.annotation.concurrent.Immutable

data class MyReviewsEntity(
    val reviews: List<MyReview>,
) {
    @Immutable
    data class MyReview(
        val reviewId: Long,
        val companyName: String,
    )
}

/**
 * Converts a FetchMyReviewResponse into a domain MyReviewsEntity.
 *
 * @return A MyReviewsEntity whose `reviews` list contains the mapped domain reviews from the response.
 */
internal fun FetchMyReviewResponse.toEntity() = MyReviewsEntity(
    reviews = this.reviews.map { it.toEntity() },
)

/**
 * Converts a network review response into a domain MyReview entity.
 *
 * @return A MyReviewsEntity.MyReview populated with the source review's `reviewId` and `companyName`.
 */
private fun FetchMyReviewResponse.MyReview.toEntity() = MyReviewsEntity.MyReview(
    reviewId = this.reviewId,
    companyName = this.companyName,
)