package team.retum.jobis.interview.schedule.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import team.retum.jobis.interview.schedule.model.InterviewScheduleUiModel
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable

@Composable
fun InterviewScheduleItem(
    interview: InterviewScheduleUiModel,
    isSelectable: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val backgroundColor = if (interview.isSelected) {
        JobisTheme.colors.inverseSurface
    } else {
        JobisTheme.colors.background
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .then(
                if (isSelectable && onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                },
            )
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
                text = "${interview.interviewType} | ${interview.location}",
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
