package team.retum.usecase.entity

import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.network.model.request.PostReviewRequest

data class PostReviewEntity(
    val interviewType: InterviewType,
    val location: InterviewLocation,
    val companyId: Long,
    val jobCode: Long,
    val interviewerCount: Int,
    val qnaElements: List<PostReviewContentEntity>,
    val question: String,
    val answer: String,
) {
    data class PostReviewContentEntity(
        val question: Long,
        val answer: String,
    )
}

fun PostReviewEntity.toPostReviewRequest() = PostReviewRequest(
    interviewType = this.interviewType,
    location = this.location,
    jobCode = this.jobCode,
    interviewerCount = this.interviewerCount,
    companyId = this.companyId,
    qnaElements = this.qnaElements.map { it.toEntity() },
    question = this.question,
    answer = this.answer,
)

private fun PostReviewEntity.PostReviewContentEntity.toEntity() =
    PostReviewRequest.PostReviewContentRequest(
        question = this.question,
        answer = this.answer,
    )
