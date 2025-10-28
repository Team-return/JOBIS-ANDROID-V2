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
    private val fetchQuestionsUseCase : FetchQuestionsUseCase,
) : BaseViewModel<PostNextReviewState, PostNextReviewSideEffect>(PostNextReviewState.getInitialState()) {

    /**
     * Fetches review questions and updates the view model state with the fetched questions on success.
     *
     * On successful retrieval, replaces the state's `questions` list with the returned questions; no state change occurs on failure.
     */
    internal fun fetchQuestions() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchQuestionsUseCase().onSuccess {
                setState {
                    state.value.copy(
                        questions = it.questions,
                    )
                }
            }
        }
    }

    /**
     * Updates the view state with Q&A elements constructed from the current questions and the provided answers.
     *
     * The state's `qnaElements` is replaced by a list of `PostReviewContent` created by zipping the current
     * questions' IDs (in their current order) with `answers`. Pairing stops at the shorter of the two lists;
     * unmatched questions or answers are ignored. Other state fields are preserved.
     *
     * @param answers User-provided answers corresponding to the current questions, in order.
     */
    internal fun setQuestion(answers: List<String>) {
        setState {
            with(state.value) {
                val updatedQuestions = questions.map { it.id }
                copy(
                    qnaElements = updatedQuestions.zip(answers).map { (q, a) ->
                        PostReviewContent(
                            question = q,
                            answer = a
                        )
                    }
                )
            }
        }
    }

    /**
     * Emits a side effect to move to the next step, supplying the current list of question–answer elements.
     */
    internal fun onNextClick() {
        with(state.value) {
            postSideEffect(
                PostNextReviewSideEffect.MoveToNext(
                    qnaElements = qnaElements
                )
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
        /**
         * Provides the default initial PostNextReviewState for the post-review flow.
         *
         * @return A PostNextReviewState with no questions, `buttonEnabled` set to `false`, an empty `answer`,
         * `answers` initialized to three empty strings, and no `qnaElements`.
         */
        fun getInitialState() = PostNextReviewState(
            questions = emptyList(),
            buttonEnabled = false,
            answer = "",
            answers = listOf("", "", ""),
            qnaElements = emptyList(),
        )
    }
}

internal sealed interface PostNextReviewSideEffect {
    data class MoveToNext(
       val qnaElements: List<PostReviewContent>,
    ): PostNextReviewSideEffect
}