package team.retum.review.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.empty.EmptyContent
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.review.ui.component.ReviewItems
import team.retum.review.viewmodel.SearchReviewsState
import team.retum.review.viewmodel.SearchReviewsViewModel

/**
 * Binds the SearchReviewsViewModel state to the search UI and wires navigation and item-selection callbacks.
 *
 * @param onBackPressed Called when the top app bar back action is triggered.
 * @param onReviewDetailClick Called with the selected review's id when a review item is clicked.
 */
@Composable
internal fun SearchReview(
    onBackPressed: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
    searchViewModel: SearchReviewsViewModel = hiltViewModel(),
) {
    val state by searchViewModel.state.collectAsStateWithLifecycle()

    SearchReviewScreen(
        state = state,
        onBackPressed = onBackPressed,
        onReviewDetailClick = onReviewDetailClick,
        onNameChange = searchViewModel::setKeyword,
    )
}

/**
 * Renders the review search screen: a top app bar, a search input bound to the provided state,
 * a list of review results when present, and an empty state message when no results exist.
 *
 * @param state Current UI state containing the search keyword, review list, and empty-state flag.
 * @param onBackPressed Callback invoked when the back action is triggered.
 * @param onReviewDetailClick Callback invoked with the selected review's id when a review item is clicked.
 * @param onNameChange Callback invoked with the updated search keyword when the user changes the input.
 */
@Composable
private fun SearchReviewScreen(
    state: SearchReviewsState,
    onBackPressed: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
    onNameChange: (String) -> Unit,
) {
    Column {
        JobisSmallTopAppBar(
            onBackPressed = onBackPressed,
        )
        JobisTextField(
            value = { state.keyword ?: "" },
            hint = stringResource(R.string.review_hint),
            onValueChange = onNameChange,
            drawableResId = JobisIcon.Search,
        )
        if (!state.showRecruitmentsEmptyContent) {
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
        } else {
            EmptyContent(
                title = stringResource(R.string.search_review_not_find),
                description = stringResource(R.string.search_review_expect),
            )
        }
    }
}