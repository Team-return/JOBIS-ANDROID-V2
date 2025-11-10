package team.retum.review.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import team.retum.review.ui.component.ReviewItem
import team.retum.review.viewmodel.SearchReviewsState
import team.retum.review.viewmodel.SearchReviewsViewModel

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
                items(
                    items = state.reviews,
                    key = { state.reviews }
                ) {reviewEntity ->
                    ReviewItem(
                        review = reviewEntity,
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
