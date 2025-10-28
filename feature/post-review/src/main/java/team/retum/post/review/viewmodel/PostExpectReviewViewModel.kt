package team.retum.post.review.viewmodel

import androidx.compose.runtime.Immutable
import dagger.hilt.android.lifecycle.HiltViewModel
import team.retum.common.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class PostExpectReviewViewModel @Inject constructor(
) : BaseViewModel<PostExpectReviewState, PostExpectReviewSideEffect>(PostExpectReviewState.getInitialState()) {

    /**
     * Update the state's answer and recompute whether the action button should be enabled.
     *
     * @param answer The new answer text to store in the state.
     */
    internal fun setAnswer(answer: String) = setState {
        setButtonEnabled(answer = answer)
        state.value.copy(answer = answer)
    }

    /**
     * Updates the saved question text and re-evaluates whether the action button should be enabled.
     *
     * @param question The new question text to store in state.
     */
    internal fun setQuestion(question: String) = setState {
        setButtonEnabled(question = question)
        state.value.copy(question = question)
    }

    /**
     * Updates the state's `buttonEnabled` flag to true when both `answer` and `question` contain non-blank text.
     *
     * @param answer The answer text to evaluate; defaults to the current state's answer.
     * @param question The question text to evaluate; defaults to the current state's question.
     */
    private fun setButtonEnabled(
        answer: String = state.value.answer,
        question: String = state.value.question,
    ) = setState {
        state.value.copy(buttonEnabled = answer.isNotBlank() && question.isNotBlank())
    }

    /**
     * Clears the current question and answer and proceeds to the next step.
     *
     * Updates the view state by setting both `question` and `answer` to empty strings and emits a
     * PostExpectReviewSideEffect.PostReview containing the cleared values.
     */
    internal fun setEmpty() {
        with(state.value) {
            copy(
                question = "",
                answer = "",
            )
        }
        onNextClick()
    }

    /**
     * Emits a PostReview side effect containing the current state's question and answer.
     *
     * The side effect can be observed to trigger the review-posting flow with the current inputs.
     */
    internal fun onNextClick() {
        with(state.value) {
            postSideEffect(PostExpectReviewSideEffect.PostReview(
                question = question,
                answer = answer
            ))
        }
    }
}

@Immutable
data class PostExpectReviewState(
    val question: String,
    val answer: String,
    val buttonEnabled: Boolean,
) {
    companion object {
        /**
         * Creates the initial state for PostExpectReview with empty inputs and the submit button disabled.
         *
         * @return A PostExpectReviewState with `question` and `answer` set to empty strings and `buttonEnabled` set to `false`.
         */
        fun getInitialState() = PostExpectReviewState(
            question = "",
            answer = "",
            buttonEnabled = false
        )
    }
}

internal sealed interface PostExpectReviewSideEffect {
    data class PostReview(
        val question: String,
        val answer: String,
    ): PostExpectReviewSideEffect
}