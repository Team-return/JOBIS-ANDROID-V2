package team.retum.post.review.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.post.review.model.PostReviewData.PostReviewContent
import team.retum.usecase.entity.QuestionsEntity.QuestionEntity
import team.retum.usecase.usecase.review.FetchQuestionsUseCase
import javax.inject.Inject

@HiltViewModel
internal class PostNextReviewViewModel @Inject constructor(
    private val fetchQuestionsUseCase: FetchQuestionsUseCase,
) : BaseViewModel<PostNextReviewState, PostNextReviewSideEffect>(PostNextReviewState.getInitialState()) {

    internal fun fetchQuestions() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchQuestionsUseCase().onSuccess {
                setState {
                    state.value.copy(
                        questions = it.questions,
                        answers = List(it.questions.size) { "" },
                    )
                }
            }.onFailure {
                postSideEffect(PostNextReviewSideEffect.FetchQuestionError)
            }
        }
    }

    internal fun setAnswer(answer: String, index: Int) {
        setState {
            with(state.value) {
                val updatedAnswers = answers.toMutableList()
                updatedAnswers[index] = answer
                copy(
                    answers = updatedAnswers,
                    buttonEnabled = answer.isNotBlank(),
                )
            }
        }
    }

    internal fun getAnswer(index: Int): String {
        return state.value.answers.getOrNull(index) ?: ""
    }

    internal fun updateButtonEnabledForPage(pageIndex: Int) {
        setState {
            state.value.copy(
                buttonEnabled = state.value.answers.getOrNull(pageIndex)?.isNotBlank() ?: false,
            )
        }
    }

    internal fun setQuestion() {
        setState {
            with(state.value) {
                val updatedQuestions = questions.map { it.id }
                copy(
                    qnaElements = updatedQuestions.zip(answers).map { (q, a) ->
                        PostReviewContent(
                            question = q,
                            answer = a,
                        )
                    },
                )
            }
        }
    }

    internal fun onNextClick() {
        with(state.value) {
            postSideEffect(
                PostNextReviewSideEffect.MoveToNext(
                    qnaElements = qnaElements,
                ),
            )
        }
    }
}

@Immutable
internal data class PostNextReviewState(
    val questions: List<QuestionEntity>,
    val buttonEnabled: Boolean,
    val answer: String,
    val answers: List<String>,
    val qnaElements: List<PostReviewContent>,
) {
    companion object {
        fun getInitialState() = PostNextReviewState(
            questions = emptyList(),
            buttonEnabled = false,
            answer = "",
            answers = emptyList(),
            qnaElements = emptyList(),
        )
    }
}

internal sealed interface PostNextReviewSideEffect {
    data class MoveToNext(
        val qnaElements: List<PostReviewContent>,
    ) : PostNextReviewSideEffect
    data object FetchQuestionError : PostNextReviewSideEffect
}
