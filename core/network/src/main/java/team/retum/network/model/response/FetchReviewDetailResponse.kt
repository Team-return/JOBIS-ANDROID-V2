package team.retum.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchReviewDetailResponse(
    @Json(name = "qna_responses") val qnaResponses: List<Detail>,
) {
    data class Detail(
        @Json(name = "question") val question: String,
        @Json(name = "answer") val answer: String,
        @Json(name = "area") val area: String,
    )
}
