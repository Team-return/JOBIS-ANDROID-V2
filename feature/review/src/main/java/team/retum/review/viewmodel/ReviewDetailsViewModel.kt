package team.retum.review.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.FetchReviewDetailEntity
import team.retum.usecase.usecase.review.FetchReviewDetailUseCase
import javax.inject.Inject

@HiltViewModel
internal class ReviewDetailsViewModel @Inject constructor(
    private val fetchReviewDetailsUseCase: FetchReviewDetailUseCase,
) : BaseViewModel<ReviewDetailsState, Unit>(ReviewDetailsState.getInitialState()) {

    internal fun setReviewId(reviewId: String) = setState {
        state.value.copy(reviewId = reviewId)
    }

    internal fun fetchReviewDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchReviewDetailsUseCase(state.value.reviewId).onSuccess {
                setState { state.value.copy(questions = it.qnaResponses) }
            }
        }
    }
}

@Immutable
internal data class ReviewDetailsState(
    val reviewId: String,
    val questions: List<FetchReviewDetailEntity.Detail>,
) {
    companion object {
        fun getInitialState() = ReviewDetailsState(
            reviewId = "",
            questions = emptyList(),
        )
    }
}
