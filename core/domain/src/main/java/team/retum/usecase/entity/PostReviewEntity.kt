package team.retum.usecase.entity

import team.retum.network.model.request.PostReviewRequest

data class PostReviewEntity(
    val companyId: Long,
    val qnaElements: List<PostReviewContentEntity>,
) {
    data class PostReviewContentEntity(
        val question: String,
        val answer: String,
        val codeId: Long,
    )
}

fun PostReviewRequest.toPostReviewEntity() = PostReviewEntity(
    companyId = this.companyId,
    qnaElements = this.qnaElements.map { it.toEntity() }
)

private fun PostReviewRequest.PostReviewContentRequest.toEntity() =
    PostReviewEntity.PostReviewContentEntity(
        question = this.question,
        answer = this.answer,
        codeId = this.codeId,
    )