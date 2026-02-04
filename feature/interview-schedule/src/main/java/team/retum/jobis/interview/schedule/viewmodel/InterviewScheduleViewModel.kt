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
) {
    companion object {
        fun getInitialState() = InterviewScheduleState(
            interviews = emptyList(),
            totalCount = 0,
        )
    }
}

internal sealed interface InterviewScheduleSideEffect {
    data object SetInterviewSuccess : InterviewScheduleSideEffect
    data object SetInterviewFailed : InterviewScheduleSideEffect
}
