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
import team.retum.review.viewmodel.ReviewFilterViewModel
import team.retum.review.viewmodel.ReviewViewModel
import team.retum.review.viewmodel.ReviewsState

@Composable
internal fun Review(
    onReviewFilterClick: () -> Unit,
    onSearchReviewClick: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
    reviewViewModel: ReviewViewModel = hiltViewModel(),
) {
    val state by reviewViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        with(reviewViewModel) {
            setYear(ReviewFilterViewModel.year)
            setCode(ReviewFilterViewModel.code)
            setLocation(ReviewFilterViewModel.location)
            setInterviewType(ReviewFilterViewModel.interviewType)
            clearReview()
            fetchReviews()
        }
    }

    ReviewScreen(
        state = state,
        onReviewFilterClick = onReviewFilterClick,
        onSearchReviewClick = onSearchReviewClick,
        onReviewDetailClick = onReviewDetailClick
    )
}

@Composable
private fun ReviewScreen(
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
                    reviewId = review.reviewId,
                    writer = review.writer,
                    major = review.major,
                    onReviewDetailClick = onReviewDetailClick,
                )
            }
        }
    }
}
