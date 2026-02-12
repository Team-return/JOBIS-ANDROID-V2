package team.retum.jobis.interview.schedule.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.retum.common.enums.HiringProgress
import team.retum.jobis.interview.schedule.viewmodel.CompanySuggestion
import team.retum.jobis.interview_schedule.R
import team.retum.jobisdesignsystemv2.checkbox.JobisCheckBox
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.jobisdesignsystemv2.utils.clickable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
internal fun InterviewScheduleForm(
    company: String,
    onCompanyChange: (String) -> Unit,
    companySuggestions: List<CompanySuggestion>,
    showCompanySuggestions: Boolean,
    onCompanySelected: (Long, String) -> Unit,
    onDismissCompanySuggestions: () -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
    hiringProgress: HiringProgress,
    showHiringProgressDropdown: Boolean,
    onHiringProgressSelected: (HiringProgress) -> Unit,
    onHiringProgressDropdownClick: () -> Unit,
    startDate: LocalDate,
    endDate: LocalDate,
    isDateRange: Boolean,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onIsDateRangeChange: (Boolean) -> Unit,
    time: String,
    onTimeChange: (String) -> Unit,
    isEditMode: Boolean = false,
) {
    Column {
        Box {
            JobisTextField(
                title = stringResource(id = R.string.company),
                value = { company },
                hint = stringResource(id = R.string.company_hint),
                onValueChange = onCompanyChange,
                fieldColor = if (isEditMode) {
                    JobisTheme.colors.surfaceVariant
                } else {
                    JobisTheme.colors.inverseSurface
                },
            )
            DropdownMenu(
                expanded = showCompanySuggestions,
                onDismissRequest = onDismissCompanySuggestions,
                containerColor = JobisTheme.colors.background,
            ) {
                companySuggestions.forEach { suggestion ->
                    DropdownMenuItem(
                        text = {
                            JobisText(
                                text = suggestion.name,
                                style = JobisTypography.Body,
                            )
                        },
                        onClick = { onCompanySelected(suggestion.id, suggestion.name) },
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 12.dp,
                ),
        ) {
            JobisText(
                modifier = Modifier.padding(bottom = 4.dp),
                text = stringResource(id = R.string.interview_type),
                style = JobisTypography.Description,
                color = JobisTheme.colors.onSurface,
            )
            Box {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 48.dp)
                        .clickable(onClick = onHiringProgressDropdownClick),
                    shape = RoundedCornerShape(12.dp),
                    color = JobisTheme.colors.inverseSurface,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 8.dp,
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val isPlaceholder = hiringProgress == HiringProgress.DOCUMENT
                        JobisText(
                            modifier = Modifier.weight(1f),
                            text = if (isPlaceholder) {
                                stringResource(id = R.string.interview_type_hint)
                            } else {
                                hiringProgress.value
                            },
                            style = JobisTypography.Body,
                            color = if (isPlaceholder) {
                                JobisTheme.colors.onSurfaceVariant
                            } else {
                                JobisTheme.colors.onBackground
                            },
                        )
                        Icon(
                            painter = painterResource(id = JobisIcon.ArrowRight),
                            contentDescription = "dropdown",
                            tint = JobisTheme.colors.onSurfaceVariant,
                        )
                    }
                }
                DropdownMenu(
                    expanded = showHiringProgressDropdown,
                    onDismissRequest = onHiringProgressDropdownClick,
                    containerColor = JobisTheme.colors.background,
                ) {
                    HiringProgress.entries.forEach { progress ->
                        DropdownMenuItem(
                            text = {
                                JobisText(
                                    text = progress.value,
                                    style = JobisTypography.Body,
                                )
                            },
                            onClick = { onHiringProgressSelected(progress) },
                        )
                    }
                }
            }
        }

        JobisTextField(
            title = stringResource(id = R.string.interview_location),
            value = { location },
            hint = stringResource(id = R.string.interview_location_hint),
            onValueChange = onLocationChange,
        )

        DateSection(
            startDate = startDate,
            endDate = endDate,
            isDateRange = isDateRange,
            onStartDateClick = onStartDateClick,
            onEndDateClick = onEndDateClick,
            onIsDateRangeChange = onIsDateRangeChange,
        )

        JobisTextField(
            title = stringResource(id = R.string.interview_time),
            value = { time },
            hint = stringResource(id = R.string.interview_time_hint),
            onValueChange = onTimeChange,
        )
    }
}

@Composable
private fun DateSection(
    startDate: LocalDate,
    endDate: LocalDate,
    isDateRange: Boolean,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onIsDateRangeChange: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp,
            ),
    ) {
        JobisText(
            modifier = Modifier.padding(bottom = 4.dp),
            text = stringResource(id = R.string.interview_date),
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurface,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DateCard(
                modifier = Modifier.weight(1f),
                date = startDate,
                onClick = onStartDateClick,
            )
            if (isDateRange) {
                JobisText(
                    text = "~",
                    style = JobisTypography.SubHeadLine,
                    color = JobisTheme.colors.onSurfaceVariant,
                )
                DateCard(
                    modifier = Modifier.weight(1f),
                    date = endDate,
                    onClick = onEndDateClick,
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            JobisText(
                text = stringResource(id = R.string.interview_period),
                style = JobisTypography.Description,
                color = JobisTheme.colors.onSurfaceVariant,
            )
            JobisCheckBox(
                checked = isDateRange,
                onClick = onIsDateRangeChange,
            )
        }
    }
}

@Composable
private fun DateCard(
    modifier: Modifier = Modifier,
    date: LocalDate,
    onClick: () -> Unit,
) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .background(
                color = JobisTheme.colors.inverseSurface,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            JobisText(
                text = date.format(dateFormatter),
                style = JobisTypography.Body,
                color = JobisTheme.colors.onBackground,
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = JobisIcon.InterviewCalendar),
                contentDescription = "calendar",
                tint = JobisTheme.colors.onSurfaceVariant,
            )
        }
    }
}
