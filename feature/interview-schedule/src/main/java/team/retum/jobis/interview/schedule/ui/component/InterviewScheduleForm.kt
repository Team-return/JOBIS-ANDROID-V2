package team.retum.jobis.interview.schedule.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
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
fun InterviewScheduleForm(
    company: String,
    onCompanyChange: (String) -> Unit,
    interviewType: String,
    onInterviewTypeChange: (String) -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
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

        JobisTextField(
            modifier = Modifier.height(92.dp),
            title = stringResource(id = R.string.interview_type),
            value = { interviewType },
            hint = stringResource(id = R.string.interview_type_hint),
            onValueChange = onInterviewTypeChange,
            singleLine = false,
            imeAction = ImeAction.Default,
        )

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
            .border(
                width = 1.dp,
                color = JobisTheme.colors.surfaceVariant,
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
            Icon(
                painter = painterResource(id = JobisIcon.Calendar),
                contentDescription = "calendar",
                tint = JobisTheme.colors.onSurfaceVariant,
            )
            JobisText(
                text = date.format(dateFormatter),
                style = JobisTypography.Body,
                color = JobisTheme.colors.onBackground,
            )
        }
    }
}
