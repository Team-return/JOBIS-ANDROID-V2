package team.retum.review.model

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType

data class PostReviewData(
    val interviewType: InterviewType = InterviewType.INDIVIDUAL,
    val location: InterviewLocation = InterviewLocation.GYEONGGI,
    val companyId: Long = 0,
    val jobCode: Long = 0,
    val interviewerCount: Int = 0,
    val qnaElements: List<PostReviewContentEntity> = emptyList(),
    val question: String = "",
    val answer: String = "",
) {
    data class PostReviewContentEntity(
        val question: Long = 0,
        val answer: String = "",
    )
}

internal fun PostReviewData.toJsonString() = Json.encodeToString(this)
internal fun String.toSignUpData() = Json.decodeFromString<PostReviewData>(this)
