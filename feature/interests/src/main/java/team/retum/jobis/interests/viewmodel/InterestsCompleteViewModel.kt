package team.retum.jobis.interests.viewmodel

import androidx.compose.runtime.Immutable
import dagger.hilt.android.lifecycle.HiltViewModel
import team.retum.common.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class InterestsCompleteViewModel @Inject constructor() : BaseViewModel<InterestsCompleteState, InterestsCompleteSideEffect>(InterestsCompleteState.getInitialState())

@Immutable
internal data class InterestsCompleteState(
    val studentName: String,
) {
    companion object {
        fun getInitialState() = InterestsCompleteState(
            studentName = "",
        )
    }
}

internal sealed class InterestsCompleteSideEffect
