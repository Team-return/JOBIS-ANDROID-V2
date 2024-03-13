package team.retum.review.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.common.component.ReviewContent
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.review.viewmodel.ReviewsState
import team.retum.review.viewmodel.ReviewsViewModel

@Composable
internal fun Reviews(
    onBackPressed: () -> Unit,
    companyId: Long,
    companyName: String,
    navigateToReviewDetails: (String, String) -> Unit,
    reviewsViewModel: ReviewsViewModel = hiltViewModel(),
) {
    val state by reviewsViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        reviewsViewModel.setCompanyId(companyId = companyId)
        reviewsViewModel.fetchReviews()
    }

    ReviewsScreen(
        onBackPressed = onBackPressed,
        companyName = companyName,
        onReviewContentClick = navigateToReviewDetails,
        state = state,
    )
}

@Composable
private fun ReviewsScreen(
    onBackPressed: () -> Unit,
    companyName: String,
    onReviewContentClick: (String, String) -> Unit,
    state: ReviewsState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisLargeTopAppBar(
            onBackPressed = onBackPressed,
            title = "${companyName}의 면접 후기",
        )
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            state.reviews.forEach {
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
