package team.retum.review.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable

@Composable
internal fun ReviewItems(
    modifier: Modifier = Modifier,
    companyImageUrl: String,
    companyName: String,
    reviewId: Long,
    writer: String,
    major: String,
    onReviewDetailClick: (Long) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = 12.dp,
                horizontal = 24.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = modifier
                .weight(1f)
                .clickable(onClick = { onReviewDetailClick(reviewId) }),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        color = if (companyImageUrl.isEmpty()) {
                            JobisTheme.colors.surfaceVariant
                        } else {
                            Color.Unspecified
                        },
                    ),
                model = companyImageUrl,
                contentDescription = "company profile",
                contentScale = ContentScale.Crop,
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                JobisText(
                    text = companyName,
                    style = JobisTypography.SubHeadLine,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                JobisText(
                    text = "$major • $writer",
                    style = JobisTypography.Description,
                    color = JobisTheme.colors.onPrimary,
                )
            }
        }
    }
}
