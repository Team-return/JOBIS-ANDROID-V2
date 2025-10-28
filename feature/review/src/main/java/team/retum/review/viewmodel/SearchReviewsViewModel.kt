package team.retum.review.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.key
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.FetchReviewsEntity
import team.retum.usecase.usecase.review.FetchReviewsUseCase
import javax.inject.Inject

const val SEARCH_DEBOUNCE_MILLIS = 1000L

@HiltViewModel
internal class SearchReviewsViewModel @Inject constructor(
    private val fetchReviewsUseCase: FetchReviewsUseCase,
) : BaseViewModel<SearchReviewsState, Unit>(SearchReviewsState.getInitialState()) {

    init {
        debounceName()
    }

    /**
     * Updates the ViewModel state with the provided search keyword.
     *
     * @param keyword The new search keyword to store in the state.
     */
    internal fun setKeyword(keyword: String) = setState {
        state.value.copy(keyword = keyword)
    }

    /**
     * Observes keyword changes and triggers a debounced search for reviews.
     *
     * Debounces keyword updates by `SEARCH_DEBOUNCE_MILLIS` and calls `fetchReviews()` when the keyword is not blank.
     */
    @OptIn(FlowPreview::class)
    private fun debounceName() {
        viewModelScope.launch {
            state.map { it.keyword }.distinctUntilChanged().debounce(SEARCH_DEBOUNCE_MILLIS).collect {
                if (!it.isNullOrBlank()) {
                    fetchReviews()
                }
            }
        }
    }

    /**
     * Fetches reviews using the current state's keyword and updates the ViewModel state with the results.
     *
     * Updates `reviews` with the fetched list and sets `showRecruitmentsEmptyContent` to `true` when the returned list is empty, `false` otherwise.
     */
    internal fun fetchReviews() {
        with(state.value) {
            viewModelScope.launch(Dispatchers.IO) {
                fetchReviewsUseCase(
                    companyId = null,
                    page = null,
                    location = null,
                    interviewType = null,
                    keyword = keyword,
                    year = null,
                    code = null
                ).onSuccess {
                    setState {
                        state.value.copy(
                            showRecruitmentsEmptyContent = it.reviews.isEmpty(),
                            reviews = it.reviews,
                        )
                    }
                }
            }
        }
    }
}

@Immutable
data class SearchReviewsState(
    val keyword: String?,
    val reviews: List<FetchReviewsEntity.Review>,
    val showRecruitmentsEmptyContent: Boolean
) {
    companion object {
        /**
         * Provides the initial SearchReviewsState for the view model.
         *
         * @return A SearchReviewsState with `keyword` set to null, an empty `reviews` list, and `showRecruitmentsEmptyContent` set to false.
         */
        fun getInitialState() = SearchReviewsState(
            keyword = null,
            reviews = emptyList(),
            showRecruitmentsEmptyContent = false,
        )
    }
}