package team.retum.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchMyReviewResponse(
    @Json(name = "reviews") val reviews: List<Review>
) {
    @JsonClass(generateAdapter = true)
    data class Review(
        @Json(name = "review_id") val reviewId: Long,
        @Json(name = "company_name") val companyName: String,
    )
}
