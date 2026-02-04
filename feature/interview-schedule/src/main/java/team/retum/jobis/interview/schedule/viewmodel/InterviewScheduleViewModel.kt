package team.retum.jobis.interview.schedule.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.HiringProgress
import team.retum.usecase.entity.interview.InterviewsEntity
import team.retum.usecase.usecase.interview.FetchInterviewUseCase
import team.retum.usecase.usecase.interview.SetInterviewUseCase
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
internal class InterviewScheduleViewModel @Inject constructor(
    private val fetchInterviewUseCase: FetchInterviewUseCase,
    private val setInterviewUseCase: SetInterviewUseCase,
) : BaseViewModel<InterviewScheduleState, InterviewScheduleSideEffect>(InterviewScheduleState.getInitialState()) {

    init {
        fetchInterviews()
    }

    private fun fetchInterviews() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchInterviewUseCase().onSuccess {
                setState {
                    state.value.copy(
                        interviews = it.interviews,
                        totalCount = it.totalCount,
                    )
                }
            }
        }
    }

    internal fun setSelectedTabIndex(index: Int) {
        setState {
            state.value.copy(selectedTabIndex = index)
        }
    }

    internal fun setSelectedDate(date: LocalDate) {
        setState {
            state.value.copy(selectedDate = date)
        }
    }

    internal fun setCurrentMonth(month: YearMonth) {
        setState {
            state.value.copy(currentMonth = month)
        }
    }

    internal fun setInterview(interviewModel: InterviewModel) {
        viewModelScope.launch(Dispatchers.IO) {
            setInterviewUseCase(
                interviewType = interviewModel.interviewType,
                startDate = interviewModel.startDate,
                endDate = interviewModel.endDate,
                interviewTime = interviewModel.interviewTime,
                companyName = interviewModel.companyName,
                location = interviewModel.location,
                studentId = interviewModel.studentId,
            ).onSuccess {
                fetchInterviews()
                postSideEffect(InterviewScheduleSideEffect.SetInterviewSuccess)
            }.onFailure {
                postSideEffect(InterviewScheduleSideEffect.SetInterviewFailed)
            }
        }
    }
}

internal data class InterviewModel(
     val interviewType: HiringProgress,
     val startDate: String,
     val endDate: String,
     val interviewTime: String,
     val companyName: String,
     val location: String,
     val studentId: Long
)

@Immutable
internal data class InterviewScheduleState(
    val interviews: List<InterviewsEntity.InterviewEntity>,
    val totalCount: Int,
    val selectedTabIndex: Int,
    val selectedDate: LocalDate,
    val currentMonth: YearMonth,
) {
    companion object {
        fun getInitialState() = InterviewScheduleState(
            interviews = emptyList(),
            totalCount = 0,
            selectedTabIndex = 0,
            selectedDate = LocalDate.now(),
            currentMonth = YearMonth.now(),
        )
    }

    fun getEventDates(): Set<LocalDate> {
        return interviews.mapNotNull { interview ->
            runCatching {
                LocalDate.parse(interview.startDate)
            }.getOrNull()
        }.toSet()
    }

    fun getInterviewsForSelectedDate(): List<InterviewsEntity.InterviewEntity> {
        return interviews.filter { interview ->
            runCatching {
                LocalDate.parse(interview.startDate) == selectedDate
            }.getOrDefault(false)
        }
    }
}

internal sealed interface InterviewScheduleSideEffect {
    data object SetInterviewSuccess : InterviewScheduleSideEffect
    data object SetInterviewFailed : InterviewScheduleSideEffect
}
