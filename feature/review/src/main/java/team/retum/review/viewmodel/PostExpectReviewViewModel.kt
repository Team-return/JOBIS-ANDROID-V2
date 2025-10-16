package team.retum.review.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

@HiltViewModel
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

    internal fun postReview() {
        viewModelScope.launch(Dispatchers.IO) {
            postReviewUseCase(
                postReviewRequest = PostReviewEntity(
                    interviewType = state.value.interviewType,
                    location = state.value.location,
                    companyId = state.value.companyId,
                    jobCode = state.value.jobCode,
                    interviewerCount = state.value.interviewerCount,
                    qnaElements = state.value.qnaElements,
                    question = state.value.question,
                    answer = state.value.answer,
                )
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
    val interviewType: InterviewType,
    val location: InterviewLocation,
    val companyId: Long,
    val jobCode: Long,
    val interviewerCount: Int,
    val qnaElements: List<PostReviewContentEntity>,
    val question: String,
    val answer: String,
    val buttonEnabled: Boolean,
) {
    companion object {
        fun getInitialState() = PostExpectReviewState(
            interviewType = InterviewType.INDIVIDUAL,
            location = InterviewLocation.DAEJEON,
            companyId = 0,
            jobCode = 0,
            interviewerCount = 0,
            qnaElements = emptyList(),
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
