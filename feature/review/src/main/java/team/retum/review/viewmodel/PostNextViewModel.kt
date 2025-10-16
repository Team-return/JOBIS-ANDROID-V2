package team.retum.review.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.exception.BadRequestException
import team.retum.usecase.entity.PostReviewEntity
import team.retum.usecase.entity.QuestionsEntity
import team.retum.usecase.entity.QuestionsEntity.QuestionEntity
import team.retum.usecase.usecase.review.FetchQuestionsUseCase
import javax.inject.Inject

@HiltViewModel
internal class PostNextViewModel @Inject constructor(
    private val fetchQuestionsUseCase : FetchQuestionsUseCase,
) : BaseViewModel<PostNextState, Unit>(PostNextState.getInitialState()) {

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
internal data class PostNextState(
    val questions: List<QuestionsEntity.QuestionEntity>,
    val buttonEnabled: Boolean,
    val answer: String,
) {
    companion object {
        fun getInitialState() = PostNextState(
            questions = emptyList(),
            buttonEnabled = false,
            answer = "",
        )
    }
}
