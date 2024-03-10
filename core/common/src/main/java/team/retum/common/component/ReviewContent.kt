package team.retum.common.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import team.retum.jobisdesignsystemv2.card.JobisCard
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText

@Composable
fun ReviewContent(
    onClick: (String, String) -> Unit,
    reviewId: String,
    writer: String,
    year: Int,
) {
    JobisCard(onClick = { onClick(reviewId, writer) }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 12.dp,
                    horizontal = 16.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            JobisText(
                text = writer,
                style = JobisTypography.SubHeadLine,
            )
            Spacer(modifier = Modifier.width(8.dp))
            JobisText(
                text = year.toString(),
                style = JobisTypography.Description,
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = JobisIcon.LongArrow),
                contentDescription = "long arrow",
                tint = JobisTheme.colors.onSurfaceVariant,
            )
        }
    }
}
