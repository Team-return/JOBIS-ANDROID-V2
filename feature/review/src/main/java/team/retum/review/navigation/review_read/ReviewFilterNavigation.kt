package team.retum.review.navigation.review_read

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.review.ui.review_read.ReviewFilter

const val NAVIGATION_REVIEW_FILTER = "reviewFilter"

fun NavGraphBuilder.reviewFilter(
    onBackPressed: () -> Unit,
) {
    composable(
        route = NAVIGATION_REVIEW_FILTER,
    ) {
        ReviewFilter(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToReviewFilter() {
    navigate(NAVIGATION_REVIEW_FILTER)
}
