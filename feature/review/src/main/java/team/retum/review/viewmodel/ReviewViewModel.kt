package team.retum.review.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.usecase.entity.FetchReviewsEntity
import team.retum.usecase.usecase.review.FetchReviewsUseCase
import javax.inject.Inject

@HiltViewModel
internal class ReviewViewModel @Inject constructor(
    private val fetchReviewsUseCase: FetchReviewsUseCase,
) : BaseViewModel<ReviewState, ReviewSideEffect>(ReviewState.getInitialState()) {

    internal fun setCode(code: Long?) = setState {
        state.value.copy(code = code)
    }

    internal fun setYear(year: Int?) = setState {
        state.value.copy(year = year)
    }

    internal fun setInterviewType(interviewType: InterviewType?) = setState {
        state.value.copy(interviewType = interviewType)
    }

    internal fun setLocation(location: InterviewLocation?) = setState {
        state.value.copy(location = location)
    }

    internal fun clearReview() {
        if (state.value.code != null || state.value.year != null) {
            setState {
                state.value.copy(
                    page = 0L,
                    reviews = emptyList(),
                )
            }
        }
    }

    internal fun fetchReviews() {
        with(state.value) {
            viewModelScope.launch(Dispatchers.IO) {
                fetchReviewsUseCase(
                    page = null,
                    location = location,
                    interviewType = interviewType,
                    companyId = null,
                    keyword = null,
                    year = year,
                    code = code,
                ).onSuccess {
                    setState { state.value.copy(reviews = it.reviews) }
                }.onFailure {
                    postSideEffect(ReviewSideEffect.FetchError)
                }
            }
        }
    }
}

internal data class ReviewState(
    val page: Long,
    val companyId: Long,
    val code: Long?,
    val year: Int?,
    val interviewType: InterviewType?,
    val location: InterviewLocation?,
    val reviews: List<FetchReviewsEntity.Review>,
) {
    companion object {
        fun getInitialState() = ReviewState(
            page = 0L,
            companyId = 0,
            code = null,
            year = null,
            interviewType = null,
            location = null,
            reviews = emptyList(),
        )
    }
}

internal sealed class ReviewSideEffect {
    data object FetchError : ReviewSideEffect()
}
