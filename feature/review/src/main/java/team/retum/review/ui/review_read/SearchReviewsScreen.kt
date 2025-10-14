package team.retum.review.ui.review_read

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.jobis.review.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.review.viewmodel.SearchReviewsState
import team.retum.review.viewmodel.SearchReviewsViewModel

@Composable
internal fun SearchReviews(
    onBackPressed: () -> Unit,
    searchViewModel: SearchReviewsViewModel = hiltViewModel(),
) {
    val state by searchViewModel.state.collectAsStateWithLifecycle()

    SearchReviewsScreen(
        state = state,
        onBackPressed = onBackPressed,
        onNameChange = searchViewModel::setName,
    )
}

@Composable
private fun SearchReviewsScreen(
    state: SearchReviewsState,
    onBackPressed: () -> Unit,
    onNameChange: (String) -> Unit,
) {
    Column {
        JobisSmallTopAppBar(
            onBackPressed = onBackPressed
        )
        JobisTextField(
            value = { state.name ?: "" },
            hint = stringResource(R.string.review_hint),
            onValueChange = onNameChange,
            drawableResId = JobisIcon.Search,
        )
    }
}
