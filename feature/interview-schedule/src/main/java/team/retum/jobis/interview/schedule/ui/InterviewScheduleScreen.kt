package team.retum.jobis.interview.schedule.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableSet
import team.retum.jobis.interview_schedule.R
import team.retum.jobis.interview.schedule.viewmodel.InterviewScheduleState
import team.retum.jobis.interview.schedule.viewmodel.InterviewScheduleViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.calendar.JobisCalendar
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.tab.TabBar
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.usecase.entity.interview.InterviewsEntity
import java.time.YearMonth

@Composable
internal fun InterviewSchedule(
    onBackPressed: () -> Unit,
    onAddClick: () -> Unit = {},
) {
    val interviewScheduleViewModel: InterviewScheduleViewModel = hiltViewModel()
    val state by interviewScheduleViewModel.state.collectAsStateWithLifecycle()

    InterviewScheduleScreen(
        onBackPressed = onBackPressed,
        onAddClick = onAddClick,
        state = state,
        onTabSelected = interviewScheduleViewModel::setSelectedTabIndex,
        onMonthChanged = interviewScheduleViewModel::setCurrentMonth,
    )
}

@Composable
private fun InterviewScheduleScreen(
    onBackPressed: () -> Unit,
    onAddClick: () -> Unit,
    state: InterviewScheduleState,
    onTabSelected: (Int) -> Unit,
    onMonthChanged: (YearMonth) -> Unit,
) {
    val tabs = persistentListOf(
        stringResource(id = R.string.tab_calendar),
        stringResource(id = R.string.tab_edit),
    )

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
            JobisCalendar(
                modifier = Modifier.padding(horizontal = 24.dp),
                initialMonth = state.currentMonth,
                eventDates = state.getEventDates().toImmutableSet(),
                onMonthChanged = onMonthChanged,
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(17.dp)
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
                        InterviewScheduleItem(interview = interview)
                    }
                }
            }
        }
        FloatingAddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            onClick = onAddClick,
        )
    }
}

@Composable
private fun InterviewScheduleItem(
    interview: InterviewsEntity.InterviewEntity,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp,
            ),
    ) {
        JobisText(
            text = interview.companyName,
            style = JobisTypography.HeadLine,
            color = JobisTheme.colors.onPrimary,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            JobisText(
                text = "${interview.interviewType.value} | ${interview.location}",
                style = JobisTypography.Body,
                color = JobisTheme.colors.onSurfaceVariant,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        JobisText(
            text = "${interview.startDate} ${interview.interviewTime}",
            style = JobisTypography.Body,
            color = JobisTheme.colors.onBackground,
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
