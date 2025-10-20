package team.retum.review.viewmodel

import androidx.compose.runtime.Immutable
import dagger.hilt.android.lifecycle.HiltViewModel
import team.retum.common.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class PostExpectReviewViewModel @Inject constructor(
) : BaseViewModel<PostExpectReviewState, PostExpectReviewSideEffect>(PostExpectReviewState.getInitialState()) {

    internal fun setAnswer(answer: String) = setState {
        setButtonEnabled(answer = answer)
        state.value.copy(answer = answer)
    }

    internal fun setQuestion(question: String) = setState {
        setButtonEnabled(question = question)
        state.value.copy(question = question)
    }

    private fun setButtonEnabled(
        answer: String = state.value.answer,
        question: String = state.value.question,
    ) = setState {
        state.value.copy(buttonEnabled = answer.isNotBlank() && question.isNotBlank())
    }

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
