package team.retum.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType

@JsonClass(generateAdapter = true)
data class FetchReviewDetailResponse(
    @Json(name = "review_id") val reviewId: String,
    @Json(name = "company_name") val companyName: String,
    @Json(name = "writer") val writer: String,
    @Json(name = "major") val major: String,
    @Json(name = "type") val type: InterviewType,
    @Json(name = "location") val location: InterviewLocation,
    @Json(name = "interviewer_count") val interviewerCount: Int,
    @Json(name = "year") val year: Int,
    @Json(name = "qn_as") val qnaResponse: List<QnAs>,
    @Json(name = "question") val question: String,
    @Json(name = "answer") val answer: String,
) {
    @JsonClass(generateAdapter = true)
    data class QnAs(
        @Json(name = "id") val id: Int,
        @Json(name = "question") val question: String,
        @Json(name = "answer") val answer: String,
    )
}
