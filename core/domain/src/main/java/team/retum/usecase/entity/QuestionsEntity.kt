package team.retum.usecase.entity

import androidx.compose.runtime.Immutable
import team.retum.network.model.response.FetchQuestionsResponse

data class QuestionsEntity(
    val questions: List<QuestionEntity>,
) {
    @Immutable
    data class QuestionEntity(
        val id: Long,
        val question: String,
    )
}

/**
 * Converts a FetchQuestionsResponse into a QuestionsEntity.
 *
 * @return A QuestionsEntity containing the converted question entities.
 */
internal fun FetchQuestionsResponse.toEntity() = QuestionsEntity( // TODO : 이름 통일
    questions = this.questions.map { it.toEntity() },
)

/**
 * Converts a network `FetchQuestionsResponse.Question` into a `QuestionsEntity.QuestionEntity`.
 *
 * @return A `QuestionsEntity.QuestionEntity` containing the same `id` and `question` text as the source.
 */
private fun FetchQuestionsResponse.Question.toEntity() = QuestionsEntity.QuestionEntity(
    id = this.id,
    question = this.question,
)