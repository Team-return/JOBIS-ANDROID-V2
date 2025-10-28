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
) : BaseViewModel<ReviewsState, Unit>(ReviewsState.getInitialState()) {

    /**
     * Updates the current ReviewsState with the provided company identifier.
     *
     * @param companyId The company identifier to store in the state.
     */
    internal fun setCompanyId(companyId: Long) = setState {
        state.value.copy(companyId = companyId)
    }

    /**
     * Sets the review code filter in the view model state.
     *
     * @param code The review code to filter by, or `null` to clear the code filter.
     */
    internal fun setCode(code: Long?) = setState {
        state.value.copy(code = code)
    }

    /**
     * Updates the state's year filter to the given value.
     *
     * @param year The interview year to filter reviews by, or `null` to clear the year filter.
     */
    internal fun setYear(year: Int?) = setState {
        state.value.copy(year = year)
    }

    /**
     * Updates the current state's interviewType filter.
     *
     * @param interviewType The interview type to apply; pass `null` to clear the filter.
     */
    internal fun setInterviewType(interviewType: InterviewType?) = setState {
        state.value.copy(interviewType = interviewType)
    }

    /**
     * Updates the current review filter's interview location in the view model state.
     *
     * @param location The interview location to apply; `null` clears the location filter.
     */
    internal fun setLocation(location: InterviewLocation?) = setState {
        state.value.copy(location = location)
    }

    /**
     * Stub that would clear review-related filters when a code or year are present.
     *
     * Currently performs no operation; the intended implementation is commented out.
     */
    internal fun clearReview() {
        if (state.value.code != null || state.value.year != null) {
            //state.value.reviews = emptyList<FetchReviewsEntity.Review>()
            //setState { state.value.copy(page = 0L) }
        }
    }

    /**
     * Fetches reviews using the current state's filters and updates the view model state with the fetched reviews.
     *
     * Calls the fetchReviews use case with the current `location`, `interviewType`, `year`, and `code` from state,
     * and replaces the state's `reviews` list with the returned reviews when the call succeeds.
     */
    internal fun fetchReviews() {
        with(state.value) {
            viewModelScope.launch(Dispatchers.IO) {
                fetchReviewsUseCase(
                    companyId = null,
                    page = null,
                    location = location,
                    interviewType = interviewType,
                    keyword = null,
                    year = year,
                    code = code
                ).onSuccess {
                    setState { state.value.copy(reviews = it.reviews) }
                }
            }
        }
    }
}

internal data class ReviewsState(
    val companyId: Long,
    val code: Long?,
    val year: Int?,
    val interviewType: InterviewType?,
    val location: InterviewLocation?,
    val reviews: List<FetchReviewsEntity.Review>,
) {
    companion object {
        /**
         * Creates the default ReviewsState used as the initial ViewModel state.
         *
         * @return A ReviewsState with companyId set to 0, code, year, interviewType and location set to null, and an empty reviews list.
         */
        fun getInitialState() = ReviewsState(
            companyId = 0,
            code = null,
            year = null,
            interviewType = null,
            location = null,
            reviews = emptyList(),
        )
    }
}