package team.retum.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostReviewRequest(
    @Json(name = "company_id") val companyId: Long,
    @Json(name = "qna_elements") val qnaElements: List<PostReviewContentRequest>,
) {
    @JsonClass(generateAdapter = true)
    data class PostReviewContentRequest(
        @Json(name = "question") val question: String,
        @Json(name = "answer") val answer: String,
        @Json(name = "code_id") val codeId: Long,
    )
}
