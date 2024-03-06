package team.retum.review.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.CodeType
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

    init {
        fetchCodes()
    }

    internal fun setQuestion(question: String) =
        setState { state.value.copy(question = question) }

    internal fun setAnswer(answer: String) =
        setState { state.value.copy(answer = answer) }

    internal fun postReview(
        companyId: Long,
        question: String,
        answer: String,
        codeId: Long,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            postReviewUseCase.invoke(
                postReviewRequest = PostReviewEntity(
                    companyId = companyId,
                    qnaElements = listOf(
                        PostReviewEntity.PostReviewContentEntity(
                            answer = answer,
                            question = question,
                            codeId = codeId,
                        ),
                    ),
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

    private fun fetchCodes() =
        viewModelScope.launch(Dispatchers.IO) {
            fetchCodeUseCase.invoke(
                keyword = null,
                type = CodeType.TECH,
                parentCode = null,
            ).onSuccess {
                techs.addAll(it.codes)
            }
        }
}

internal data class ReviewState(
    val question: String,
    val answer: String,
) {
    companion object {
        fun getDefaultState() = ReviewState(
            question = "",
            answer = "",
        )
    }
}

internal sealed interface ReviewSideEffect {
    data object BadRequest : ReviewSideEffect

    data object Success : ReviewSideEffect
}
