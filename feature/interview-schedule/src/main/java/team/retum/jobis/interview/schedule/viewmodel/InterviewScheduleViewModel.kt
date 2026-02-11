package team.retum.jobis.interview.schedule.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.interview.InterviewsEntity
import team.retum.usecase.usecase.interview.FetchInterviewUseCase
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
internal class InterviewScheduleViewModel @Inject constructor(
    private val fetchInterviewUseCase: FetchInterviewUseCase,
) : BaseViewModel<InterviewScheduleState, InterviewScheduleSideEffect>(InterviewScheduleState.getInitialState()) {

    internal fun fetchInterviews() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchInterviewUseCase().onSuccess {
                setState {
                    state.value.copy(
                        interviews = it.interviews,
                        totalCount = it.totalCount,
                    )
                }
            }.onFailure {
                postSideEffect(InterviewScheduleSideEffect.FetchInterviewFailed)
            }
        }
    }

    internal fun setSelectedTabIndex(index: Int) {
        setState {
            state.value.copy(selectedTabIndex = index)
        }
    }

    internal fun setCurrentMonth(month: YearMonth) {
        setState {
            state.value.copy(currentMonth = month)
        }
    }

    internal fun setSelectedInterviewId(interviewId: Long?) {
        setState {
            state.value.copy(
                selectedInterviewId = if (state.value.selectedInterviewId == interviewId) {
                    null
                } else {
                    interviewId
                }
            )
        }
    }
}

@Immutable
internal data class InterviewScheduleState(
    val interviews: List<InterviewsEntity.InterviewEntity>,
    val totalCount: Int,
    val selectedTabIndex: Int,
    val selectedDate: LocalDate,
    val currentMonth: YearMonth,
    val selectedInterviewId: Long?,
) {
    companion object {
        fun getInitialState() = InterviewScheduleState(
            interviews = emptyList(),
            totalCount = 0,
            selectedTabIndex = 0,
            selectedDate = LocalDate.now(),
            currentMonth = YearMonth.now(),
            selectedInterviewId = null,
        )
    }

    fun getEventDates(): Set<LocalDate> {
        return interviews.mapNotNull { interview ->
            runCatching {
                LocalDate.parse(interview.startDate)
            }.getOrNull()
        }.toSet()
    }
}

internal sealed interface InterviewScheduleSideEffect {
    data object FetchInterviewFailed : InterviewScheduleSideEffect
}
