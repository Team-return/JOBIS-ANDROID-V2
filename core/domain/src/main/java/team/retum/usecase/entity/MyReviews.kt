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

internal fun FetchMyReviewResponse.toEntity() = MyReviewsEntity(
    reviews = this.reviews.map { it.toEntity() },
)

private fun FetchMyReviewResponse.MyReview.toEntity() = MyReviewsEntity.MyReview(
    reviewId = this.reviewId,
    companyName = this.companyName,
)
