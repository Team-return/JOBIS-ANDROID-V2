package team.retum.review.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.review.model.PostReviewData
import team.retum.usecase.entity.QuestionsEntity.QuestionEntity
import team.retum.usecase.usecase.review.FetchQuestionsUseCase
import javax.inject.Inject

@HiltViewModel
internal class PostNextReviewViewModel @Inject constructor(
    private val fetchQuestionsUseCase : FetchQuestionsUseCase,
) : BaseViewModel<PostNextReviewState, PostNextReviewSideEffect>(PostNextReviewState.getInitialState()) {

    internal fun fetchQuestions() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchQuestionsUseCase().onSuccess {
                setState {
                    state.value.copy(
                        questions = it.questions
                    )
                }
            }
        }
    }

    internal fun onNextClick() {
        with(state.value) {
            postSideEffect(PostNextReviewSideEffect.MoveToNext(
                qnaElements = qnaElements
            ))
        }
    }

    internal fun setAnswer(answer: String) = setState {
        setButtonEnabled(answer = answer)
        state.value.copy(answer = answer)
    }

    private fun setButtonEnabled(
        answer: String = state.value.answer,
    ) = setState {
        state.value.copy(buttonEnabled = answer.isNotBlank())
    }
}

@Immutable
internal data class PostNextReviewState(
    val questions: List<QuestionEntity>,
    val buttonEnabled: Boolean,
    val answer: String,
    val qnaElements: List<PostReviewData.PostReviewContent>,
) {
    companion object {
        fun getInitialState() = PostNextReviewState(
            questions = emptyList(),
            buttonEnabled = false,
            answer = "",
            qnaElements = emptyList(),
        )
    }
}

internal sealed interface PostNextReviewSideEffect {
    data class MoveToNext(
       val qnaElements: List<PostReviewData.PostReviewContent>,
    ): PostNextReviewSideEffect
}
