package team.retum.review.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import team.retum.common.component.ReviewContent
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.usecase.entity.FetchReviewsEntity

@Composable
internal fun Reviews(
    onBackPressed: () -> Unit,
    companyId: Long,
    companyName: String,
    navigateToReviewDetails: (String, String) -> Unit,
) {
    ReviewsScreen(
        onBackPressed = onBackPressed,
        companyName = companyName,
        reviews = emptyList(),
        onReviewContentClick = navigateToReviewDetails,
    )
}

@Composable
private fun ReviewsScreen(
    onBackPressed: () -> Unit,
    companyName: String,
    reviews: List<FetchReviewsEntity.Review>,
    onReviewContentClick: (String, String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisLargeTopAppBar(
            onBackPressed = onBackPressed,
            title = companyName,
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            reviews.forEach {
                ReviewContent(
                    onClick = onReviewContentClick,
                    reviewId = it.reviewId,
                    writer = it.writer,
                    year = it.year,
                )
            }
        }
    }
}
