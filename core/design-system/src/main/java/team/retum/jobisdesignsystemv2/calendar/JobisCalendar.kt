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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.launch
import team.retum.design_system.R
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/**
 * JOBIS에서 사용하는 캘린더 컴포넌트
 *
 * @param modifier [JobisCalendar]에 적용될 [Modifier]
 * @param initialMonth 캘린더에 처음 표시될 월
 * @param eventDates 이벤트가 있는 날짜 목록
 * @param weekendTextColor 주말 텍스트 색상
 * @param weekdaysTextColor 평일 텍스트 색상
 * @param onMonthChanged 월 변경 시 호출되는 콜백
 */
@Composable
fun JobisCalendar(
    modifier: Modifier = Modifier,
    initialMonth: YearMonth = YearMonth.now(),
    eventDates: ImmutableSet<LocalDate> = persistentSetOf(),
    weekendTextColor: Color = JobisTheme.colors.surfaceTint,
    weekdaysTextColor: Color = JobisTheme.colors.onSurfaceVariant,
    onMonthChanged: ((YearMonth) -> Unit)? = null,
) {
    val rememberedInitialMonth = remember { initialMonth }
    val initialPage = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState(initialPage = initialPage) { Int.MAX_VALUE }

    var currentMonth by remember { mutableStateOf(rememberedInitialMonth) }

    LaunchedEffect(pagerState.currentPage) {
        val monthOffset = pagerState.currentPage - initialPage
        val newMonth = rememberedInitialMonth.plusMonths(monthOffset.toLong())
        if (currentMonth != newMonth) {
            currentMonth = newMonth
            onMonthChanged?.invoke(newMonth)
        }
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
        )
        Spacer(modifier = Modifier.height(27.dp))
        CalendarWeekDay(
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
            val month = rememberedInitialMonth.plusMonths(monthOffset.toLong())

            CalendarMonthContent(
                modifier = Modifier.fillMaxWidth(),
                month = month,
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
                contentDescription = stringResource(id = R.string.content_description_previous_month),
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
                contentDescription = stringResource(id = R.string.content_description_next_month),
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
private fun CalendarWeekDay(
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
private fun CalendarMonthContent(
    modifier: Modifier = Modifier,
    month: YearMonth,
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
                weekDates.forEachIndexed { _, date ->
                    CalendarDayCell(
                        date = date,
                        displayMonth = month,
                        hasEvent = eventDates.contains(date),
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
    hasEvent: Boolean,
) {
    val isToday = date == LocalDate.now()
    val isCurrentMonthDisplayed = date.month == displayMonth.month && date.year == displayMonth.year
    val showEventHighlight = hasEvent && isCurrentMonthDisplayed

    val textColor = when {
        showEventHighlight -> JobisTheme.colors.background
        isToday -> JobisTheme.colors.primaryContainer
        !isCurrentMonthDisplayed -> JobisTheme.colors.surfaceTint
        else -> JobisTheme.colors.onSurfaceVariant
    }

    val backgroundColor = if (showEventHighlight) JobisTheme.colors.onPrimary else JobisTheme.colors.surface
    val borderColor = if (isToday && !showEventHighlight) JobisTheme.colors.onPrimary else JobisTheme.colors.surface

    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(
                width = if (isToday && !showEventHighlight) 1.dp else 0.dp,
                color = borderColor,
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = JobisTypography.Body,
            color = textColor,
            textAlign = TextAlign.Center,
        )
    }
}
