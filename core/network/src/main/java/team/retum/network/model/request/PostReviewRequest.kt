package team.retum.network.model.request

import com.google.gson.annotations.SerializedName

data class PostReviewRequest(
    @SerializedName("company_id") val companyId: Long,
    @SerializedName("qna_elements") val qnaElements: List<PostReviewContentRequest>,
) {
    data class PostReviewContentRequest(
        @SerializedName("question") val question: String,
        @SerializedName("answer") val answer: String,
        @SerializedName("code_id") val codeId: Long,
    )
}
