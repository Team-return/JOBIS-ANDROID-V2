package team.retum.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchReviewsResponse(
    @Json(name = "reviews") val reviews: List<Review>,
) {
    @JsonClass(generateAdapter = true)
    data class Review(
        @Json(name = "review_id") val reviewId: String,
        @Json(name = "year") val year: Int,
        @Json(name = "writer") val writer: String,
        @Json(name = "date") val date: String,
    )
}
