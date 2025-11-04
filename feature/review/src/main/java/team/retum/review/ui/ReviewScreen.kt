package team.retum.review.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.review.ui.component.ReviewItems
import team.retum.review.viewmodel.ReviewFilterViewModel
import team.retum.review.viewmodel.ReviewSideEffect
import team.retum.review.viewmodel.ReviewState
import team.retum.review.viewmodel.ReviewViewModel

@Composable
internal fun Review(
    onReviewFilterClick: () -> Unit,
    onSearchReviewClick: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
    reviewViewModel: ReviewViewModel = hiltViewModel(),
) {
    val state by reviewViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        with(reviewViewModel) {
            setYear(ReviewFilterViewModel.year)
            setCode(ReviewFilterViewModel.code)
            setLocation(ReviewFilterViewModel.location)
            setInterviewType(ReviewFilterViewModel.interviewType)
            clearReview()
            fetchReviews()
        }

        reviewViewModel.sideEffect.collect {
            when (it) {
                is ReviewSideEffect.FetchError -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.review_fetch_error),
                        drawable = JobisIcon.Error,
                    ).show()
                }
            }
        }
    }

    ReviewScreen(
        state = state,
        onReviewFilterClick = onReviewFilterClick,
        onSearchReviewClick = onSearchReviewClick,
        onReviewDetailClick = onReviewDetailClick,
    )
}

@Composable
private fun ReviewScreen(
    state: ReviewState,
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
                contentDescription = stringResource(id = R.string.content_description_filter),
                onClick = onReviewFilterClick,
                tint = JobisTheme.colors.onPrimary,
            )
            JobisIconButton(
                drawableResId = JobisIcon.Search,
                contentDescription = stringResource(id = R.string.content_description_search),
                onClick = onSearchReviewClick,
            )
        }
        LazyColumn {
            items(
                items = state.reviews,
                key = { it.reviewId },
            ) {
                ReviewItems(
                    companyImageUrl = it.companyLogoUrl,
                    companyName = it.companyName,
                    reviewId = it.reviewId,
                    writer = it.writer,
                    major = it.major,
                    onReviewDetailClick = onReviewDetailClick,
                )
            }
        }
    }
}
