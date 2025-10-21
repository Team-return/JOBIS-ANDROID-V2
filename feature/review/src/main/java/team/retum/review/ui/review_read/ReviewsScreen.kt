package team.retum.review.ui.review_read

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.review.ui.component.ReviewItems
import team.retum.review.viewmodel.ReviewsFilterViewModel
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
        with(reviewsViewModel) {
            setYear(ReviewsFilterViewModel.year)
            setCode(ReviewsFilterViewModel.code)
            setLocation(ReviewsFilterViewModel.location)
            setInterviewType(ReviewsFilterViewModel.interviewType)
            clearReview()
            fetchReviews()
        }
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
                ReviewItems(
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
