package team.retum.network.model.response

import com.google.gson.annotations.SerializedName

data class FetchReviewsResponse(
    @SerializedName("reviews") val reviews: List<Review>,
) {
    data class Review(
        @SerializedName("review_id") val reviewId: String,
        @SerializedName("year") val year: Int,
        @SerializedName("writer") val writer: String,
        @SerializedName("date") val date: String,
    )
}
