package team.retum.review.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.CodeType
import team.retum.common.enums.ReviewProcess
import team.retum.common.exception.BadRequestException
import team.retum.usecase.entity.CodesEntity
import team.retum.usecase.entity.PostReviewEntity
import team.retum.usecase.usecase.code.FetchCodeUseCase
import team.retum.usecase.usecase.review.PostReviewUseCase
import javax.inject.Inject

@HiltViewModel
internal class ReviewViewModel @Inject constructor(
    private val postReviewUseCase: PostReviewUseCase,
    private val fetchCodeUseCase: FetchCodeUseCase,
) : BaseViewModel<ReviewState, ReviewSideEffect>(ReviewState.getDefaultState()) {

    var techs: SnapshotStateList<CodesEntity.CodeEntity> = mutableStateListOf()
        private set

    var reviews: SnapshotStateList<PostReviewEntity.PostReviewContentEntity> = mutableStateListOf()
        private set

    var keywords: SnapshotStateList<String> = mutableStateListOf()
        private set

    internal fun setInit() =
        setState {
            state.value.copy(
                question = "",
                answer = "",
                keyword = "",
                checked = "",
                selectedTech = 0,
                tech = null,
                buttonEnabled = false,
                reviewProcess = ReviewProcess.QUESTION,
            )
        }

    internal fun setQuestion(question: String) {
        setState { state.value.copy(question = question) }
        setButtonEnabled()
    }

    internal fun setAnswer(answer: String) {
        setState { state.value.copy(answer = answer) }
        setButtonEnabled()
    }

    internal fun setKeyword(keyword: String) {
        techs.clear()
        setState { state.value.copy(keyword = keyword) }
        setButtonEnabled()
    }

    internal fun addReview() {
        reviews.add(
            PostReviewEntity.PostReviewContentEntity(
                answer = state.value.answer,
                question = state.value.question,
                codeId = state.value.selectedTech,
            ),
        )
    }

    internal fun setSelectedTech(selectedTech: Long) =
        setState { state.value.copy(selectedTech = selectedTech) }

    internal fun postReview(companyId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            postReviewUseCase(
                postReviewRequest = PostReviewEntity(
                    companyId = companyId,
                    qnaElements = reviews,
                ),
            ).onSuccess {
                postSideEffect(ReviewSideEffect.Success)
            }.onFailure {
                when (it) {
                    is BadRequestException -> {
                        postSideEffect(ReviewSideEffect.BadRequest)
                    }
                }
            }
        }
    }

    internal fun fetchCodes(keyword: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            fetchCodeUseCase(
                keyword = keyword,
                type = CodeType.TECH,
                parentCode = null,
            ).onSuccess {
                techs.addAll(it.codes)
            }
        }

    internal fun setReviewProcess(reviewProcess: ReviewProcess) {
        setState { state.value.copy(reviewProcess = reviewProcess) }
        setButtonEnabled()
    }

    private fun setButtonEnabled() {
        when (state.value.reviewProcess) {
            ReviewProcess.QUESTION -> {
                setState { state.value.copy(buttonEnabled = state.value.question.isNotEmpty() && state.value.answer.isNotEmpty()) }
            }

            else -> {
                setState { state.value.copy(buttonEnabled = state.value.checked.isNotEmpty()) }
            }
        }
    }

    internal fun setChecked(checked: String) {
        setState { state.value.copy(checked = checked) }
        setButtonEnabled()
    }
}

internal data class ReviewState(
    val question: String,
    val answer: String,
    val keyword: String,
    val checked: String,
    val selectedTech: Long,
    val tech: String?,
    val buttonEnabled: Boolean,
    val reviewProcess: ReviewProcess,
) {
    companion object {
        fun getDefaultState() = ReviewState(
            question = "",
            answer = "",
            keyword = "",
            checked = "",
            selectedTech = 0,
            tech = null,
            buttonEnabled = false,
            reviewProcess = ReviewProcess.QUESTION,
        )
    }
}

internal sealed interface ReviewSideEffect {
    data object BadRequest : ReviewSideEffect

    data object Success : ReviewSideEffect
}
