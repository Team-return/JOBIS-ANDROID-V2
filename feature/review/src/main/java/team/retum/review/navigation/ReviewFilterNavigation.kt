package team.retum.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.review.ui.ReviewFilter

const val NAVIGATION_REVIEW_FILTER = "reviewFilter"

/**
 * Adds a navigation destination for the review filter screen to this NavGraphBuilder.
 *
 * The destination is registered under the route `NAVIGATION_REVIEW_FILTER` and displays
 * the ReviewFilter UI, forwarding the provided back-press callback.
 *
 * @param onBackPressed Called when the user requests to navigate back from the review filter screen.
 */
fun NavGraphBuilder.reviewFilter(
    onBackPressed: () -> Unit,
) {
    composable(
        route = NAVIGATION_REVIEW_FILTER,
    ) {
        ReviewFilter(onBackPressed = onBackPressed)
    }
}

/**
 * Navigate to the Review Filter screen.
 */
fun NavController.navigateToReviewFilter() {
    navigate(NAVIGATION_REVIEW_FILTER)
}