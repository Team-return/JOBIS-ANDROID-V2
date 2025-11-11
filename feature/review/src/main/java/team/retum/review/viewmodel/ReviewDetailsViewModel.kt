package team.retum.review.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.usecase.entity.FetchReviewDetailEntity
import team.retum.usecase.usecase.review.FetchReviewDetailUseCase
import javax.inject.Inject

@HiltViewModel
internal class ReviewDetailsViewModel @Inject constructor(
    private val fetchReviewDetailsUseCase: FetchReviewDetailUseCase,
) : BaseViewModel<ReviewDetailsState, ReviewDetailsSideEffect>(ReviewDetailsState.getInitialState()) {

    internal fun setReviewId(reviewId: Long) = setState {
        state.value.copy(reviewId = reviewId)
    }

    internal fun fetchReviewDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchReviewDetailsUseCase(state.value.reviewId)
                .onSuccess {
                    setState { state.value.copy(reviewDetail = it) }
                }.onFailure {
                    postSideEffect(ReviewDetailsSideEffect.FetchReviewDetailsError)
                }
        }
    }

    internal fun setTabIndex(tabIndex: Int) {
        setState { state.value.copy(selectedTabIndex = tabIndex) }
    }
}

@Immutable
internal data class ReviewDetailsState(
    val selectedTabIndex: Int,
    val reviewId: Long,
    val reviewDetail: FetchReviewDetailEntity,
) {
    companion object {
        fun getInitialState() = ReviewDetailsState(
            selectedTabIndex = 0,
            reviewId = 0L,
            reviewDetail = FetchReviewDetailEntity(
                reviewId = 0L,
                companyName = "",
                writer = "",
                major = "",
                type = InterviewType.INDIVIDUAL,
                location = InterviewLocation.GYEONGGI,
                interviewerCount = 0,
                year = 0,
                qnaResponse = emptyList(),
                question = "",
                answer = "",
            ),
        )
    }
}

internal sealed interface ReviewDetailsSideEffect {
    data object FetchReviewDetailsError : ReviewDetailsSideEffect
}
