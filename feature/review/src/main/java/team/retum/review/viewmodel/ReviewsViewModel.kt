package team.retum.review.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.FetchReviewsEntity
import team.retum.usecase.usecase.review.FetchReviewsUseCase
import javax.inject.Inject

@HiltViewModel
internal class ReviewsViewModel @Inject constructor(
    private val fetchReviewsUseCase: FetchReviewsUseCase,
) : BaseViewModel<ReviewsState, Unit>(ReviewsState.getInitialState()) {

    internal fun setCompanyId(companyId: Long) = setState {
        state.value.copy(companyId = companyId)
    }

    internal fun fetchReviews() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchReviewsUseCase(companyId = state.value.companyId).onSuccess {
                setState { state.value.copy(reviews = it.reviews) }
            }
        }
    }
}

internal data class ReviewsState(
    val companyId: Long,
    val reviews: List<FetchReviewsEntity.Review>,
) {
    companion object {
        fun getInitialState() = ReviewsState(
            companyId = 0,
            reviews = emptyList(),
        )
    }
}
