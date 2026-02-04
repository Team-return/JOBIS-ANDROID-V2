package team.retum.jobis.interview.schedule.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobis.interview.schedule.viewmodel.InterviewScheduleState
import team.retum.jobis.interview.schedule.viewmodel.InterviewScheduleViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisTheme

@Composable
internal fun InterviewSchedule(
    onBackPressed: () -> Unit,
) {
    val interviewScheduleViewModel: InterviewScheduleViewModel = hiltViewModel()
    val state by interviewScheduleViewModel.state.collectAsStateWithLifecycle()

    InterviewScheduleScreen(
        onBackPressed = onBackPressed,
        state = state,
    )
}

@Composable
private fun InterviewScheduleScreen(
    onBackPressed: () -> Unit,
    state: InterviewScheduleState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(
            title = "",
            onBackPressed = onBackPressed,
        )
    }
}
