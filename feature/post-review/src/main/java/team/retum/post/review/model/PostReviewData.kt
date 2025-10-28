package team.retum.post.review.model

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

/**
 * Serializes this PostReviewData to JSON and URL-encodes the resulting string using UTF-8.
 *
 * @return The URL-encoded JSON representation of this PostReviewData.
 */
internal fun PostReviewData.toJsonString(): String {
    val json = Json.encodeToString(this)
    return URLEncoder.encode(json, "UTF-8")
}

/**
 * Decode a URL-encoded JSON string and parse it into a PostReviewData.
 *
 * @receiver The URL-encoded JSON string representing a PostReviewData.
 * @return The parsed PostReviewData instance.
 */
internal fun String.toReviewData(): PostReviewData {
    val decoded = URLDecoder.decode(this, "UTF-8")
    return Json.decodeFromString<PostReviewData>(decoded)
}

/**
     * Converts this PostReviewContent to a PostReviewEntity.PostReviewContentEntity.
     *
     * @return A PostReviewEntity.PostReviewContentEntity containing the same `question` and `answer`.
     */
    internal fun PostReviewData.PostReviewContent.toEntity(): PostReviewEntity.PostReviewContentEntity =
    PostReviewEntity.PostReviewContentEntity(
        question = this.question,
        answer = this.answer,
    )