package team.retum.jobis.interview.schedule.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableSet
import team.retum.jobis.interview.schedule.model.toUiModel
import team.retum.jobis.interview.schedule.ui.component.InterviewScheduleItem
import team.retum.jobis.interview.schedule.viewmodel.InterviewScheduleSideEffect
import team.retum.jobis.interview.schedule.viewmodel.InterviewScheduleState
import team.retum.jobis.interview.schedule.viewmodel.InterviewScheduleViewModel
import team.retum.jobis.interview_schedule.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.calendar.JobisCalendar
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.tab.TabBar
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.toast.JobisToast
import java.time.YearMonth

@Composable
internal fun InterviewSchedule(
    onBackPressed: () -> Unit,
    onAddClick: () -> Unit = {},
    onEditClick: (Long) -> Unit = {},
) {
    val interviewScheduleViewModel: InterviewScheduleViewModel = hiltViewModel()
    val state by interviewScheduleViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            interviewScheduleViewModel.fetchInterviews()
        }
    }

    LaunchedEffect(Unit) {
        interviewScheduleViewModel.sideEffect.collect {
            when (it) {
                is InterviewScheduleSideEffect.FetchInterviewFailed -> JobisToast.create(
                    context = context,
                    message = context.getString(R.string.fetch_interview_fail),
                ).show()
            }
        }
    }

    InterviewScheduleScreen(
        onBackPressed = onBackPressed,
        onAddClick = onAddClick,
        onEditClick = {
            state.selectedInterviewId?.let { interviewId ->
                onEditClick(interviewId)
            }
        },
        state = state,
        onTabSelected = interviewScheduleViewModel::setSelectedTabIndex,
        onMonthChanged = interviewScheduleViewModel::setCurrentMonth,
        onInterviewSelected = interviewScheduleViewModel::setSelectedInterviewId,
    )
}

@Composable
private fun InterviewScheduleScreen(
    onBackPressed: () -> Unit,
    onAddClick: () -> Unit,
    onEditClick: () -> Unit,
    state: InterviewScheduleState,
    onTabSelected: (Int) -> Unit,
    onMonthChanged: (YearMonth) -> Unit,
    onInterviewSelected: (Long) -> Unit,
) {
    val tabs = persistentListOf(
        stringResource(id = R.string.tab_calendar),
        stringResource(id = R.string.tab_edit),
    )
    val isEditTab = state.selectedTabIndex == 1

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            JobisSmallTopAppBar(
                title = stringResource(id = R.string.schedule_management),
                onBackPressed = onBackPressed,
            )
            TabBar(
                selectedTabIndex = state.selectedTabIndex,
                tabs = tabs,
                onSelectTab = onTabSelected,
            )

            if (isEditTab) {
                EditTabContent(
                    state = state,
                    onInterviewSelected = onInterviewSelected,
                    onEditClick = onEditClick,
                )
            } else {
                CalendarTabContent(
                    state = state,
                    onMonthChanged = onMonthChanged,
                )
            }
        }

        if (!isEditTab) {
            FloatingAddButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                onClick = onAddClick,
            )
        }
    }
}

@Composable
private fun CalendarTabContent(
    state: InterviewScheduleState,
    onMonthChanged: (YearMonth) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        JobisCalendar(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            initialMonth = state.currentMonth,
            eventDates = state.getEventDates().toImmutableSet(),
            onMonthChanged = onMonthChanged,
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .background(JobisTheme.colors.inverseSurface),
        )
        if (state.interviews.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                JobisText(
                    text = stringResource(id = R.string.interview_schedule_empty),
                    style = JobisTypography.Body,
                    color = JobisTheme.colors.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(
                    items = state.interviews,
                    key = { it.id },
                ) { interview ->
                    InterviewScheduleItem(
                        interview = interview.toUiModel(),
                        isSelectable = false,
                    )
                }
            }
        }
    }
}

@Composable
private fun EditTabContent(
    state: InterviewScheduleState,
    onInterviewSelected: (Long) -> Unit,
    onEditClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        JobisText(
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 16.dp,
            ),
            text = stringResource(id = R.string.scheduled_interviews),
            style = JobisTypography.SubHeadLine,
            color = JobisTheme.colors.onBackground,
        )

        if (state.interviews.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                JobisText(
                    text = stringResource(id = R.string.interview_schedule_empty),
                    style = JobisTypography.Body,
                    color = JobisTheme.colors.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(
                    items = state.interviews,
                    key = { it.id },
                ) { interview ->
                    InterviewScheduleItem(
                        interview = interview.toUiModel(
                            isSelected = interview.id == state.selectedInterviewId
                        ),
                        isSelectable = true,
                        onClick = { onInterviewSelected(interview.id) },
                    )
                }
            }
        }

        JobisButton(
            text = stringResource(id = R.string.edit_button),
            color = ButtonColor.Primary,
            enabled = state.selectedInterviewId != null,
            onClick = onEditClick,
        )
    }
}

@Composable
private fun FloatingAddButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier.size(56.dp),
        onClick = onClick,
        shape = CircleShape,
        containerColor = JobisTheme.colors.onPrimary,
        contentColor = JobisTheme.colors.background,
    ) {
        Icon(
            painter = painterResource(id = JobisIcon.Plus),
            contentDescription = "Add",
            tint = JobisTheme.colors.background,
        )
    }
}
