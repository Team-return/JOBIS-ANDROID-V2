package team.retum.review.ui.review_read

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.textfield.JobisTextField

@Composable
internal fun SearchReviews(
    onBackPressed: () -> Unit,
) {
    SearchReviewsScreen(onBackPressed = onBackPressed)
}

@Composable

private fun SearchReviewsScreen(
    onBackPressed: () -> Unit,
    onNameChange: (String) -> Unit = {},
) {
    Column {
        JobisSmallTopAppBar(
            onBackPressed = onBackPressed
        )
        JobisTextField(
            value = { "" },
            hint = "직무를 선택해주세요",
            onValueChange = onNameChange,
            drawableResId = JobisIcon.Search,
        )
    }
}
