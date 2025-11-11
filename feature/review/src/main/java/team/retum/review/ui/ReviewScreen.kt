package team.retum.review.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.appbar.JobisLargeTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.review.ui.component.ReviewItems
import team.retum.review.viewmodel.ReviewFilterViewModel
import team.retum.review.viewmodel.ReviewSideEffect
import team.retum.review.viewmodel.ReviewViewModel
import team.retum.usecase.entity.FetchReviewsEntity

@Composable
internal fun Review(
    onReviewFilterClick: () -> Unit,
    onSearchReviewClick: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
    reviewViewModel: ReviewViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        with(reviewViewModel) {
            setCode(ReviewFilterViewModel.code)
            setYear(ReviewFilterViewModel.year)
            setInterviewType(ReviewFilterViewModel.interviewType)
            setLocation(ReviewFilterViewModel.location)
            clearReviews()
            fetchTotalReviewCount()
        }
    }

    LaunchedEffect(Unit) {
        reviewViewModel.sideEffect.collect {
            when (it) {
                is ReviewSideEffect.FetchErrorCount -> JobisToast.create(
                    context = context,
                    message = context.getString(R.string.review_page_fetch_error),
                    drawable = JobisIcon.Error,
                ).show()

                is ReviewSideEffect.FetchErrorReview -> JobisToast.create(
                    context = context,
                    message = context.getString(R.string.review_fetch_error),
                    drawable = JobisIcon.Error,
                ).show()

                is ReviewSideEffect.SetReplaceReviewError -> JobisToast.create(
                    context = context,
                    message = context.getString(R.string.review_replace_error),
                    drawable = JobisIcon.Error,
                ).show()
            }
        }
    }

    ReviewScreen(
        reviews = reviewViewModel.reviews.toPersistentList(),
        onReviewFilterClick = onReviewFilterClick,
        onSearchReviewClick = onSearchReviewClick,
        onReviewDetailClick = onReviewDetailClick,
        whetherFetchNextPage = reviewViewModel::whetherFetchNextPage,
        fetchNextPage = reviewViewModel::fetchReviews,
    )
}

@Composable
private fun ReviewScreen(
    reviews: ImmutableList<FetchReviewsEntity.Review>,
    onReviewFilterClick: () -> Unit,
    onSearchReviewClick: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
    whetherFetchNextPage: (Int) -> Boolean,
    fetchNextPage: () -> Unit,
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
        ReviewItems(
            reviews = reviews,
            onReviewDetailClick = onReviewDetailClick,
            whetherFetchNextPage = whetherFetchNextPage,
            fetchNextPage = fetchNextPage,
        )
    }
}
