package team.retum.jobisdesignsystemv2.review

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

/**
 * Displays a tappable card showing a review's author and year.
 *
 * @param onClick Callback invoked with [reviewId] when the card is clicked.
 * @param reviewId Identifier for the review that will be passed to [onClick].
 * @param writer Text displayed as the review's author.
 * @param year Text displayed as the review's year.
 */
@Composable
fun ReviewContent(
    onClick: (Long) -> Unit,
    reviewId: Long,
    writer: String,
    year: String,
) {
    JobisCard(onClick = { onClick(reviewId) }) {
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
                text = year,
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