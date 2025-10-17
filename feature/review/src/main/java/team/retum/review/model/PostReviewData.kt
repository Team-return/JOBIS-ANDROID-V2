package team.retum.review.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType

@Serializable
data class PostReviewData(
    val interviewType: InterviewType = InterviewType.INDIVIDUAL,
    val location: InterviewLocation = InterviewLocation.GYEONGGI,
    val companyId: Long = 0,
    val jobCode: Long = 0,
    val interviewerCount: Int = 0,
    val qnaElements: List<PostReviewContent> = emptyList(),
    val question: String = "",
    val answer: String = "",
) {
    @Serializable
    data class PostReviewContent(
        val question: Long = 0,
        val answer: String = "",
    )
}

internal fun PostReviewData.toJsonString() = Json.encodeToString(this)
internal fun String.toReviewData() = Json.decodeFromString<PostReviewData>(this)
