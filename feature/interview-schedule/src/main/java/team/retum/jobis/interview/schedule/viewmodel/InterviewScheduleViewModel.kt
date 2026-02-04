package team.retum.jobis.interview.schedule.viewmodel

import androidx.compose.runtime.Immutable
import dagger.hilt.android.lifecycle.HiltViewModel
import team.retum.common.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class InterviewScheduleViewModel @Inject constructor(
    
) : BaseViewModel<InterviewScheduleState, InterviewScheduleSideEffect>(InterviewScheduleState.getInitialState()) {
}

@Immutable
internal data class InterviewScheduleState(
    val placeholder: String,
) {
    companion object {
        fun getInitialState() = InterviewScheduleState(
            placeholder = "",
        )
    }
}

internal sealed interface InterviewScheduleSideEffect
