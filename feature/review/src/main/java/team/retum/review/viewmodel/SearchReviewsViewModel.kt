package team.retum.review.viewmodel

import androidx.compose.runtime.Immutable
import dagger.hilt.android.lifecycle.HiltViewModel
import team.retum.common.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class SearchReviewsViewModel @Inject constructor(
) : BaseViewModel<SearchReviewsState, Unit>(SearchReviewsState.getInitialState()) {

    internal fun setName(name: String) = setState {
        state.value.copy(name = name)
    }
}

@Immutable
data class SearchReviewsState(
    val name: String?
) {
    companion object {
        fun getInitialState() = SearchReviewsState(
            name = null,
        )
    }
}
