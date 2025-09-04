package team.retum.review.ui.review_read

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.review.viewmodel.ReviewsState
import team.retum.review.viewmodel.ReviewsViewModel

@Composable
internal fun Reviews(
    onReviewFilterClick: () -> Unit,
    onSearchReviewClick: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
    reviewsViewModel: ReviewsViewModel = hiltViewModel(),
) {
    val state by reviewsViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
       reviewsViewModel.fetchReviews()
    }

    ReviewsScreen(
        state = state,
        onReviewFilterClick = onReviewFilterClick,
        onSearchReviewClick = onSearchReviewClick,
        onReviewDetailClick = onReviewDetailClick
    )
}

@Composable
private fun ReviewsScreen(
    state: ReviewsState,
    onReviewFilterClick: () -> Unit,
    onSearchReviewClick: () -> Unit,
    onReviewDetailClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisLargeTopAppBar(title = stringResource(id = R.string.review)) {
            JobisIconButton(
                drawableResId = JobisIcon.Filter,
                contentDescription = "filter",
                onClick = onReviewFilterClick,
                tint = JobisTheme.colors.onPrimary,
            )
            JobisIconButton(
                drawableResId = JobisIcon.Search,
                contentDescription = "search",
                onClick = onSearchReviewClick,
            )
        }
        LazyColumn {
            items(state.reviews.size) {
                val review = state.reviews[it]
                ReviewItem(
                    companyImageUrl = review.companyLogoUrl,
                    companyName = review.companyName,
                    reviewId = review.reviewId.toLong(),
                    writer = review.writer,
                    major = review.major,
                    onReviewDetailClick = onReviewDetailClick,
                )
            }
        }
    }
}

@Composable
private fun ReviewItem(
    modifier: Modifier = Modifier,
    companyImageUrl: String,
    companyName: String,
    reviewId: Long,
    writer: String,
    major: String,
    onReviewDetailClick: (Long) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = modifier
                .weight(1f)
                .padding(vertical = 16.dp)
                .clickable(onClick = { onReviewDetailClick(reviewId) }),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = companyImageUrl,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                contentDescription = "company image",
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                JobisText(
                    text = companyName,
                    style = JobisTypography.SubHeadLine,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                JobisText(
                    text = "$major•$writer",
                    style = JobisTypography.Description,
                    color = JobisTheme.colors.onPrimary,
                )
            }
        }
    }
}
