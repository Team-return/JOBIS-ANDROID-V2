package team.retum.network.model.response

import com.google.gson.annotations.SerializedName

data class FetchReviewDetailResponse(
    @SerializedName("qna_responses") val qnaResponses: List<Detail>,
) {
    data class Detail(
        @SerializedName("question") val question: String,
        @SerializedName("answer") val answer: String,
        @SerializedName("area") val area: String,
    )
}
