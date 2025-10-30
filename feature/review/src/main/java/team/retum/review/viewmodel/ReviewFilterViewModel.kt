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

    internal fun getLocalYears() {
        val startYear = 2020
        val endYear = LocalDate.now().year + 1
        viewModelScope.launch(Dispatchers.IO) {
            setState {
                state.value.copy(
                    years = (startYear..endYear).toList().reversed(),
                )
            }
        }
    }

    internal fun setSelectedMajor(majorCode: Long?) {
        setState {
            state.value.copy(
                selectedMajorCode = if (state.value.selectedMajorCode == majorCode) null else majorCode,
            )
        }
    }

    internal fun setSelectedYear(year: Int?) {
        setState {
            state.value.copy(
                selectedYear = if (state.value.selectedYear == year) null else year,
            )
        }
    }

    internal fun setSelectedInterviewType(type: InterviewType?) {
        setState {
            state.value.copy(
                selectedInterviewType = if (state.value.selectedInterviewType == type) null else type,
            )
        }
    }

    internal fun setSelectedLocation(location: InterviewLocation?) {
        setState {
            state.value.copy(
                selectedLocation = if (state.value.selectedLocation == location) null else location,
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
