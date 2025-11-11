package team.retum.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchMyReviewResponse(
    @Json(name = "reviews") val reviews: List<MyReview>,
) {
    @JsonClass(generateAdapter = true)
    data class MyReview(
        @Json(name = "review_id") val reviewId: Long,
        @Json(name = "company_name") val companyName: String,
    )
}
