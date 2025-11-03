package team.retum.usecase.entity

import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.network.model.response.FetchReviewDetailResponse

data class FetchReviewDetailEntity(
    val reviewId: Long,
    val companyName: String,
    val writer: String,
    val major: String,
    val type: InterviewType,
    val location: InterviewLocation,
    val interviewerCount: Int,
    val year: Int,
    val qnaResponse: List<QnAs>,
    val question: String,
    val answer: String,
) {
    data class QnAs(
        val id: Int,
        val question: String,
        val answer: String,
    )
}

internal fun FetchReviewDetailResponse.toEntity() = FetchReviewDetailEntity(
    reviewId = this.reviewId,
    companyName = this.companyName,
    writer = this.writer,
    major = this.major,
    type = this.type,
    location = this.location,
    interviewerCount = this.interviewerCount,
    year = this.year,
    qnaResponse = this.qnaResponse.map { it.toEntity() },
    question = this.question,
    answer = this.answer,
)

private fun FetchReviewDetailResponse.QnAs.toEntity() = FetchReviewDetailEntity.QnAs(
    id = this.id,
    question = this.question,
    answer = this.answer,
)
