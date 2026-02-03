package team.retum.jobisdesignsystemv2.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import team.retum.jobisdesignsystemv2.foundation.JobisDesignSystemV2Theme
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.utils.clickable
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JobisCalendar(
    modifier: Modifier = Modifier,
    weekendTextColor: Color = JobisTheme.colors.surfaceTint,
    weekdaysTextColor: Color = JobisTheme.colors.onSurfaceVariant,
    selectedDate: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit,
    eventDates: Set<LocalDate> = emptySet(),
) {
    val initialPage = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState(initialPage = initialPage) { Int.MAX_VALUE }

    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    LaunchedEffect(pagerState.currentPage) {
        val monthOffset = pagerState.currentPage - initialPage
        currentMonth = YearMonth.now().plusMonths(monthOffset.toLong())
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(JobisTheme.colors.surface)
            .padding(horizontal = 12.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CalendarHeader(
            modifier = Modifier.fillMaxWidth(),
            displayedMonth = currentMonth,
            pagerState = pagerState,
            initialPage = initialPage,
        )
        Spacer(modifier = Modifier.height(27.dp))
        CalendarWeekDayRow(
            modifier = Modifier.fillMaxWidth(),
            weekendTextColor = weekendTextColor,
            weekdaysTextColor = weekdaysTextColor,
        )
        Spacer(modifier = Modifier.height(26.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            contentPadding = PaddingValues(horizontal = 0.dp),
        ) { page ->
            val monthOffset = page - initialPage
            val month = YearMonth.now().plusMonths(monthOffset.toLong())

            CalendarMonthPage(
                modifier = Modifier.fillMaxWidth(),
                month = month,
                selectedDate = selectedDate,
                onDateSelected = onDateSelected,
                eventDates = eventDates,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CalendarHeader(
    modifier: Modifier = Modifier,
    displayedMonth: YearMonth,
    pagerState: PagerState,
    initialPage: Int,
) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            },
            enabled = pagerState.canScrollBackward,
        ) {
            Image(
                painter = painterResource(id = JobisIcon.Arrow),
                contentDescription = "Previous Month",
                modifier = Modifier.size(24.dp),
            )
        }
        Text(
            text = displayedMonth.format(DateTimeFormatter.ofPattern("M월")),
            style = JobisTypography.HeadLine,
            color = JobisTheme.colors.inverseOnSurface,
        )
        IconButton(
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            },
            enabled = pagerState.canScrollForward,
        ) {
            Image(
                painter = painterResource(id = JobisIcon.ArrowRight),
                contentDescription = "Next Month",
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
private fun CalendarWeekDayRow(
    modifier: Modifier = Modifier,
    weekendTextColor: Color,
    weekdaysTextColor: Color,
) {
    val weekdays = listOf("일", "월", "화", "수", "목", "금", "토")
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        weekdays.forEachIndexed { index, day ->
            val textColor = when (index) {
                0, 6 -> weekendTextColor // Sunday or Saturday
                else -> weekdaysTextColor
            }
            Text(
                text = day,
                style = JobisTypography.Body,
                color = textColor,
                modifier = Modifier.size(20.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun CalendarMonthPage(
    modifier: Modifier = Modifier,
    month: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    eventDates: Set<LocalDate>,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Determine the first day of the calendar grid (could be from previous month)
        val firstDayOfMonth = month.atDay(1)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 0 for Sunday, 1 for Monday...
        val firstDayToDisplay = firstDayOfMonth.minusDays(firstDayOfWeek.toLong())

        // Calculate total cells needed: offset days + days in month, rounded up to complete weeks
        val daysInMonth = month.lengthOfMonth()
        val totalCells = firstDayOfWeek + daysInMonth
        val weeksNeeded = (totalCells + 6) / 7
        val datesToDisplay = weeksNeeded * 7

        // Create a list of dates to display in the grid
        val dates = (0 until datesToDisplay).map {
            firstDayToDisplay.plusDays(it.toLong())
        }

        dates.chunked(7).forEachIndexed { weekIndex, weekDates ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                weekDates.forEachIndexed { dayOfWeekIndex, date ->
                    CalendarDayCell(
                        date = date,
                        displayMonth = month,
                        selectedDate = selectedDate,
                        onDateSelected = onDateSelected,
                        hasEvent = eventDates.contains(date),
                        isWeekend = dayOfWeekIndex == 0 || dayOfWeekIndex == 6,
                    )
                }
            }
            if (weekIndex < weeksNeeded - 1) {
                Spacer(modifier = Modifier.height(26.dp))
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    date: LocalDate,
    displayMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    hasEvent: Boolean,
    isWeekend: Boolean,
) {
    val isToday = date == LocalDate.now()
    val isSelected = date == selectedDate
    val isCurrentMonthDisplayed = date.month == displayMonth.month && date.year == displayMonth.year

    val textColor = when {
        isSelected -> JobisTheme.colors.background
        isToday -> JobisTheme.colors.primaryContainer
        !isCurrentMonthDisplayed -> JobisTheme.colors.surfaceTint
        isWeekend -> JobisTheme.colors.surfaceTint
        else -> JobisTheme.colors.onSurfaceVariant
    }

    val backgroundColor = if (isSelected) JobisTheme.colors.primaryContainer else JobisTheme.colors.surface
    val borderColor = if (isToday && !isSelected) JobisTheme.colors.primaryContainer else JobisTheme.colors.surface

    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(
                width = if (isToday && !isSelected) 1.dp else 0.dp,
                color = borderColor,
                shape = CircleShape,
            )
            .clickable(onClick = { onDateSelected(date) }),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = JobisTypography.Body,
            color = textColor,
            textAlign = TextAlign.Center,
        )
        if (hasEvent && isCurrentMonthDisplayed) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 2.dp)
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(JobisTheme.colors.primaryContainer),
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewJobisCalendar() {
    // Use a fixed date for initial selection
    val initialSelectedDate = LocalDate.of(2026, 2, 2)
    var selectedDate by remember { mutableStateOf(initialSelectedDate) }

    // Use fixed dates for events
    val eventDates = setOf(
        LocalDate.of(2026, 2, 5), // Today + 3 days (relative to a fixed point)
        LocalDate.of(2026, 2, 15), // Current month 15th (relative to a fixed point)
        LocalDate.of(2026, 1, 10), // Previous month 10th (relative to a fixed point)
    )

    JobisDesignSystemV2Theme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            JobisCalendar(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                eventDates = eventDates,
            )
        }
    }
}
