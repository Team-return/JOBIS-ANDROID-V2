package team.retum.post.review.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.exception.BadRequestException
import team.retum.post.review.model.PostReviewData
import team.retum.post.review.model.toEntity
import team.retum.usecase.entity.PostReviewEntity
import team.retum.usecase.usecase.review.PostReviewUseCase
import javax.inject.Inject

@HiltViewModel
internal class PostExpectReviewViewModel @Inject constructor(
    private val postReviewUseCase: PostReviewUseCase,
) : BaseViewModel<PostExpectReviewState, PostExpectReviewSideEffect>(PostExpectReviewState.getInitialState()) {

    internal fun setAnswer(answer: String) {
        setState { state.value.copy(answer = answer) }
        setButtonEnabled(answer = answer)
    }

    internal fun setQuestion(question: String) {
        setState { state.value.copy(question = question) }
        setButtonEnabled(question = question)
    }

    private fun setButtonEnabled(
        answer: String = state.value.answer,
        question: String = state.value.question,
    ) {
        setState { state.value.copy(buttonEnabled = answer.isNotBlank() && question.isNotBlank()) }
    }

    internal fun setEmpty() {
        setState {
            state.value.copy(
                question = "",
                answer = "",
            )
        }
        onNextClick()
    }

    internal fun postReview(reviewData: PostReviewData) {
        viewModelScope.launch(Dispatchers.IO) {
            postReviewUseCase(
                postReviewRequest = PostReviewEntity(
                    interviewType = reviewData.interviewType,
                    location = reviewData.location,
                    companyId = reviewData.companyId,
                    jobCode = reviewData.jobCode,
                    interviewerCount = reviewData.interviewerCount,
                    qnaElements = reviewData.qnaElements.map { it.toEntity() },
                    question = reviewData.question,
                    answer = reviewData.answer,
                ),
            ).onSuccess {
                postSideEffect(PostExpectReviewSideEffect.Success)
            }.onFailure {
                when (it) {
                    is BadRequestException -> {
                        postSideEffect(PostExpectReviewSideEffect.BadRequest)
                    }
                }
            }
        }
    }

    internal fun onNextClick() {
        with(state.value) {
            postSideEffect(
                PostExpectReviewSideEffect.PostReview(
                    question = question,
                    answer = answer,
                ),
            )
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
            buttonEnabled = false,
        )
    }
}

internal sealed interface PostExpectReviewSideEffect {
    data class PostReview(
        val question: String,
        val answer: String,
    ) : PostExpectReviewSideEffect

    data object Success : PostExpectReviewSideEffect

    data object BadRequest : PostExpectReviewSideEffect
}
