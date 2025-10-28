package team.retum.review.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.CodeType
import team.retum.common.enums.InterviewLocation
import team.retum.common.enums.InterviewType
import team.retum.usecase.entity.CodesEntity
import team.retum.usecase.usecase.code.FetchCodeUseCase
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class ReviewFilterViewModel @Inject constructor(
    private val fetchCodeUseCase: FetchCodeUseCase,
) : BaseViewModel<ReviewsFilterState, Unit>(initialState = ReviewsFilterState.getDefaultState()) {

    companion object {
        var code: Long? = null
        var year: Int? = null
        var interviewType: InterviewType? = null
        var location: InterviewLocation? = null
    }

    init {
        fetchCodes()
    }

    /**
     * Fetches job-type codes and updates the view state’s `majorList` with the fetched codes on success.
     *
     * If the fetch succeeds, the current `ReviewsFilterState` is replaced with a copy whose `majorList`
     * contains the retrieved codes.
     */
    private fun fetchCodes() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCodeUseCase(
                keyword = null,
                type = CodeType.JOB,
                parentCode = null,
            ).onSuccess {
                setState {
                    state.value.copy(
                        majorList = it.codes,
                    )
                }
            }
        }
    }

    /**
     * Sets the state's `years` to the inclusive range from 2020 through the next calendar year.
     */
    internal fun getLocalYears() {
        val startYear = 2020
        val endYear = LocalDate.now().year + 1
        viewModelScope.launch(Dispatchers.IO) {
            setState {
                state.value.copy(
                    years = (startYear..endYear).toList(),
                )
            }
        }
    }

    /**
     * Toggles the selected major code in the view state.
     *
     * If `majorCode` equals the currently selected major code, the selection is cleared; otherwise the selection is set to `majorCode`.
     *
     * @param majorCode The major code to select or `null` to clear selection.
     */
    internal fun setSelectedMajor(majorCode: Long?) {
        setState {
            state.value.copy(
                selectedMajorCode = if (state.value.selectedMajorCode == majorCode) null else majorCode
            )
        }
    }

    /**
     * Toggles the selected year filter: selects the given year or clears the selection if it's already selected.
     *
     * @param year The year to select; if it matches the current selection the selection is cleared.
     */
    internal fun setSelectedYear(year: Int?) {
        setState {
            state.value.copy(
                selectedYear = if (state.value.selectedYear == year) null else year
            )
        }
    }

    /**
     * Toggles the selected interview type filter.
     *
     * @param type The interview type to select; if it equals the current selection the selection is cleared. Passing `null` clears the selection.
     */
    internal fun setSelectedInterviewType(type: InterviewType?) {
        setState {
            state.value.copy(
                selectedInterviewType = if (state.value.selectedInterviewType == type) null else type
            )
        }
    }

    /**
     * Toggles the selected interview location filter.
     *
     * @param location The interview location to select. If this equals the current selection, the selection is cleared (set to `null`); otherwise it becomes the new selection.
     */
    internal fun setSelectedLocation(location: InterviewLocation?) {
        setState {
            state.value.copy(
                selectedLocation = if (state.value.selectedLocation == location) null else location
            )
        }
    }
}

@Immutable
data class ReviewsFilterState(
    val years: List<Int>,
    val majorList: List<CodesEntity.CodeEntity>,
    val selectedMajorCode: Long? = null,
    val selectedYear: Int? = null,
    val selectedInterviewType: InterviewType? = null,
    val selectedLocation: InterviewLocation? = null,
) {
    companion object {
        /**
         * Creates the default ReviewsFilterState with no available options and no selections.
         *
         * @return A ReviewsFilterState whose `years` and `majorList` are empty lists and whose selected fields (`selectedMajorCode`, `selectedYear`, `selectedInterviewType`, `selectedLocation`) are `null`.
         */
        fun getDefaultState() = ReviewsFilterState(
            years = emptyList(),
            majorList = emptyList(),
            selectedMajorCode = null,
            selectedYear = null,
            selectedInterviewType = null,
            selectedLocation = null,
        )
    }
}
