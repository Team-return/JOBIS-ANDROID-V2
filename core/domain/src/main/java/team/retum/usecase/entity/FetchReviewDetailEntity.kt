package team.retum.usecase.entity

import team.retum.network.model.response.FetchReviewDetailResponse

data class FetchReviewDetailEntity(
    val qnaResponses: List<Detail>,
) {
    data class Detail(
        val question: String,
        val answer: String,
        val area: String,
    )
}

internal fun FetchReviewDetailResponse.toEntity() = FetchReviewDetailEntity(
    qnaResponses = this.qnaResponses.map { it.toEntity() },
)

private fun FetchReviewDetailResponse.Detail.toEntity() = FetchReviewDetailEntity.Detail(
    question = this.question,
    answer = this.answer,
    area = this.area,
)
