package team.retum.review.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.usecase.entity.PostReviewEntity
import java.net.URLDecoder
import java.net.URLEncoder

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

internal fun PostReviewData.toJsonString(): String {
    val json = Json.encodeToString(this)
    return URLEncoder.encode(json, "UTF-8")
}

internal fun String.toReviewData(): PostReviewData {
    val decoded = URLDecoder.decode(this, "UTF-8")
    return Json.decodeFromString<PostReviewData>(decoded)
}

internal fun PostReviewData.PostReviewContent.toEntity(): PostReviewEntity.PostReviewContentEntity =
    PostReviewEntity.PostReviewContentEntity(
        question = this.question,
        answer = this.answer,
    )
