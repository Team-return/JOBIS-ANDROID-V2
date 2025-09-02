package team.retum.usecase.entity

import androidx.compose.runtime.Immutable
import team.retum.network.model.response.FetchQuestionsResponse

data class QuestionsEntity(
    val questions: List<QuestionEntity>
) {
    @Immutable
    data class QuestionEntity(
        val id: Long,
        val question: String,
    )
}

internal fun FetchQuestionsResponse.toEntity() = QuestionsEntity( // TODO : 이름 통일
    questions = this.questions.map { it.toEntity() },
)

private fun FetchQuestionsResponse.Question.toEntity() = QuestionsEntity.QuestionEntity(
    id = this.id,
    question = this.question,
)
