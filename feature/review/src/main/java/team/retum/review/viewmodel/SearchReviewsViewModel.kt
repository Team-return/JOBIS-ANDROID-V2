package team.retum.review.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
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
) : BaseViewModel<SearchReviewsState, SearchReviewsSideEffect>(SearchReviewsState.getInitialState()) {

    init {
        debounceName()
    }

    internal fun setKeyword(keyword: String) = setState {
        state.value.copy(keyword = keyword)
    }

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

    private fun fetchReviews() {
        with(state.value) {
            viewModelScope.launch(Dispatchers.IO) {
                fetchReviewsUseCase(
                    companyId = null,
                    page = null,
                    location = null,
                    interviewType = null,
                    keyword = keyword,
                    year = null,
                    code = null,
                ).onSuccess {
                    setState {
                        state.value.copy(
                            showRecruitmentsEmptyContent = it.reviews.isEmpty(),
                            reviews = it.reviews.toPersistentList(),
                        )
                    }
                }.onFailure {
                    postSideEffect(SearchReviewsSideEffect.FetchError)
                }
            }
        }
    }
}

@Immutable
data class SearchReviewsState(
    val keyword: String?,
    val reviews: ImmutableList<FetchReviewsEntity.Review>,
    val showRecruitmentsEmptyContent: Boolean,
) {
    companion object {
        fun getInitialState() = SearchReviewsState(
            keyword = null,
            reviews = persistentListOf(),
            showRecruitmentsEmptyContent = false,
        )
    }
}

internal sealed class SearchReviewsSideEffect {
    data object FetchError : SearchReviewsSideEffect()
}
