package team.retum.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType

@JsonClass(generateAdapter = true)
data class PostReviewRequest(
    @Json(name = "interview_type") val interviewType: InterviewType,
    @Json(name = "location") val location: InterviewLocation,
    @Json(name = "company_id") val companyId: Long,
    @Json(name = "job_code") val jobCode: Long,
    @Json(name = "interviewer_count") val interviewerCount: Int,
    @Json(name = "qnas") val qnaElements: List<PostReviewContentRequest>,
    @Json(name = "question") val question: String,
    @Json(name = "answer") val answer: String,
) {
    @JsonClass(generateAdapter = true)
    data class PostReviewContentRequest(
        @Json(name = "question_id") val question: String,
        @Json(name = "answer") val answer: String,
    )
}
