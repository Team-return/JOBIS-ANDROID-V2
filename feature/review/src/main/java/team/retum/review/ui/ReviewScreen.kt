package team.retum.review.ui

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

/**
 * Binds the review UI to its ViewModel, initializes filter values and review data, and displays the ReviewScreen.
 *
 * On first composition this sets filter fields from ReviewFilterViewModel, clears any existing reviews, and triggers loading of reviews, then renders the screen using the collected state.
 *
 * @param onReviewFilterClick Callback invoked when the filter action is requested.
 * @param onSearchReviewClick Callback invoked when the search action is requested.
 * @param onReviewDetailClick Callback invoked with the selected review's id when a review item is tapped.
 */
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
        onReviewDetailClick = onReviewDetailClick,
    )
}

/**
 * Renders the reviews screen UI with a top app bar and a scrollable list of review items.
 *
 * @param state The current UI state containing the list of reviews and related view data.
 * @param onReviewFilterClick Invoked when the filter action in the top app bar is clicked.
 * @param onSearchReviewClick Invoked when the search action in the top app bar is clicked.
 * @param onReviewDetailClick Invoked with a review id when a review item is selected.
 */
@Composable
private fun ReviewScreen(
    state: ReviewsState,
    onReviewFilterClick: () -> Unit,
    onSearchReviewClick: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
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