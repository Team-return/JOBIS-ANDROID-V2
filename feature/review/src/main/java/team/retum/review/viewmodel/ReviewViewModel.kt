package team.retum.review.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.CodeType
import team.retum.common.exception.BadRequestException
import team.retum.usecase.entity.CodesEntity
import team.retum.usecase.entity.FetchReviewDetailEntity
import team.retum.usecase.entity.FetchReviewsEntity
import team.retum.usecase.entity.PostReviewEntity
import team.retum.usecase.usecase.code.FetchCodeUseCase
import team.retum.usecase.usecase.review.FetchReviewDetailUseCase
import team.retum.usecase.usecase.review.FetchReviewsUseCase
import team.retum.usecase.usecase.review.PostReviewUseCase
import javax.inject.Inject

@HiltViewModel
internal class ReviewViewModel @Inject constructor(
    private val postReviewUseCase: PostReviewUseCase,
    private val fetchReviewsUseCase: FetchReviewsUseCase,
    private val fetchCodeUseCase: FetchCodeUseCase,
    private val fetchReviewDetailUseCase: FetchReviewDetailUseCase,
) : BaseViewModel<ReviewState, ReviewSideEffect>(ReviewState.getDefaultState()) {

    private var _reviews: MutableState<FetchReviewsEntity> =
        mutableStateOf(FetchReviewsEntity(emptyList()))
    val reviews: MutableState<FetchReviewsEntity> get() = _reviews

    var techs: SnapshotStateList<CodesEntity.CodeEntity> = mutableStateListOf()
        private set

    //Todo 기획 바뀔 예정
    private var _detail: MutableList<List<FetchReviewDetailEntity.Detail>> =
        mutableListOf(mutableStateListOf(FetchReviewDetailEntity.Detail("", "", "")))
    val detail: MutableList<List<FetchReviewDetailEntity.Detail>> get() = _detail

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
                fetchReviews(companyId)
            }.onFailure {
                when (it) {
                    is BadRequestException -> {
                        postSideEffect(ReviewSideEffect.BadRequest)
                    }
                }
            }
        }
    }

    internal fun fetchReviews(companyId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchReviewsUseCase.invoke(companyId = companyId).onSuccess {
                _reviews.value = it
            }.onFailure {
                if (it == BadRequestException) {
                    postSideEffect(ReviewSideEffect.BadRequest)
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

    internal fun fetchReviewDetail(reviewId: String) =
        viewModelScope.launch(Dispatchers.IO) {
            fetchReviewDetailUseCase.invoke(reviewId = reviewId).onSuccess {
                _detail.add(it.qnaResponses)
            }.onFailure {
                if (it == BadRequestException) {
                    postSideEffect(ReviewSideEffect.BadRequest)
                }
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
