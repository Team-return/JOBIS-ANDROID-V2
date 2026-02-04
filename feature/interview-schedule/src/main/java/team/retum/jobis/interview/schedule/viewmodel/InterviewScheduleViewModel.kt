package team.retum.jobis.interview.schedule.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.interview.InterviewsEntity
import team.retum.usecase.usecase.interview.FetchInterviewUseCase
import javax.inject.Inject

@HiltViewModel
internal class InterviewScheduleViewModel @Inject constructor(
    private val fetchInterviewUseCase: FetchInterviewUseCase,
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
}

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

internal sealed interface InterviewScheduleSideEffect
