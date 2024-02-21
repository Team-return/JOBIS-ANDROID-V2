package team.retum.jobis.recruitment.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.retum.jobis.recruitment.R
import team.retum.jobis.recruitment.ui.Recruitment
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable
import java.text.DecimalFormat

@Composable
internal fun RecruitmentContent(
    recruitment: Recruitment,
    onClick: (recruitId: Long) -> Unit,
) {
    val middleText = StringBuilder().apply {
        append(stringResource(R.string.military))
        append(if (recruitment.military) " O " else " X ")
        append(" · ")
        append(stringResource(R.string.recruitment))
        append(" ")
        append(DecimalFormat().format(recruitment.trainPay))
    }.toString()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 12.dp,
                horizontal = 24.dp,
            ),
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable(
                    enabled = true,
                    onClick = { onClick(recruitment.recruitId) },
                    onPressed = {},
                ),
        ) {
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
                painter = painterResource(JobisIcon.Information),
                contentDescription = "company profile",
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                JobisText(
                    text = recruitment.title,
                    style = JobisTypography.SubHeadLine,
                )
                JobisText(
                    text = middleText,
                    style = JobisTypography.SubBody,
                    color = JobisTheme.colors.inverseOnSurface,
                )
                JobisText(
                    text = recruitment.company,
                    style = JobisTypography.Description,
                    color = JobisTheme.colors.inverseOnSurface,
                )
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        JobisIconButton(
            modifier = Modifier.padding(4.dp),
            painter = painterResource(
                id = if (recruitment.isBookmarked) {
                    JobisIcon.BookmarkOn
                } else {
                    JobisIcon.BookmarkOff
                },
            ),
            contentDescription = "bookmark",
            onClick = { },
        )
    }
}
