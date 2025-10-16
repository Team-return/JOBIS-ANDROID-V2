package team.retum.review.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.common.exception.BadRequestException
import team.retum.usecase.entity.PostReviewEntity
import team.retum.usecase.entity.PostReviewEntity.PostReviewContentEntity
import team.retum.usecase.usecase.review.PostReviewUseCase
import javax.inject.Inject

internal class PostExpectReviewViewModel @Inject constructor(
    private val postReviewUseCase: PostReviewUseCase,
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

    internal fun postReview(interviewType: InterviewType, location: InterviewLocation, companyId: Long, jobCode: Long, interviewerCount: Int, qnaElements: List<PostReviewContentEntity>, question: String, answer: String) {
        viewModelScope.launch(Dispatchers.IO) {
            postReviewUseCase(
                postReviewRequest = PostReviewEntity(
                    interviewType = interviewType,
                    location = location,
                    companyId = companyId,
                    jobCode = jobCode,
                    interviewerCount = interviewerCount,
                    qnaElements = qnaElements,
                    question = question,
                    answer = answer,
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
    data object BadRequest : PostExpectReviewSideEffect

    data object Success : PostExpectReviewSideEffect
}
