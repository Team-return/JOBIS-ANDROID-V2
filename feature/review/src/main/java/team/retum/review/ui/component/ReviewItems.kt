package team.retum.review.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import kotlinx.collections.immutable.ImmutableList
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.usecase.entity.FetchReviewsEntity

@Composable
internal fun ReviewItems(
    reviews: ImmutableList<FetchReviewsEntity.Review>,
    onReviewDetailClick: (Long) -> Unit,
    whetherFetchNextPage: (Int) -> Boolean,
    fetchNextPage: () -> Unit,
) {
    LazyColumn {
        items(
            count = reviews.size,
            key = { index -> index },
        ) { index ->
            ReviewItem(
                review = reviews[index],
                onReviewDetailClick = onReviewDetailClick,
            )
            if (whetherFetchNextPage(index)) {
                fetchNextPage()
            }
        }
    }
}

@Composable
internal fun ReviewItem(
    review: FetchReviewsEntity.Review,
    onReviewDetailClick: (Long) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 12.dp,
                horizontal = 24.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable(
                    onClick = { onReviewDetailClick(review.reviewId) },
                    enabled = review.reviewId != 0L,
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        color = if (review.companyLogoUrl.isEmpty()) {
                            JobisTheme.colors.surfaceVariant
                        } else {
                            Color.Unspecified
                        },
                    ),
                model = review.companyLogoUrl,
                contentDescription = "company profile",
                contentScale = ContentScale.Crop,
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                JobisText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            color = if (review.companyName.isBlank()) {
                                JobisTheme.colors.surfaceVariant
                            } else {
                                Color.Unspecified
                            },
                        ),
                    text = review.companyName,
                    style = JobisTypography.SubHeadLine,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                JobisText(
                    text = "${review.major} • ${review.writer}",
                    style = JobisTypography.Description,
                    color = JobisTheme.colors.onPrimary,
                )
            }
        }
    }
}
