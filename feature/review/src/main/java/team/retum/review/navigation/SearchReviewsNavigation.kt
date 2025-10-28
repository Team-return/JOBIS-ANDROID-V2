package team.retum.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.review.ui.SearchReview

const val NAVIGATION_SEARCH_REVIEW = "searchReview"

/**
 * Adds a composable navigation route for the search review screen.
 *
 * Composes the SearchReview UI when navigating to `NAVIGATION_SEARCH_REVIEW` and forwards user interaction callbacks.
 *
 * @param onBackPressed Callback invoked when the user requests to navigate back.
 * @param onReviewDetailClick Callback invoked with the selected review's ID when a review item is clicked.
 */
fun NavGraphBuilder.searchReview(
    onBackPressed: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
) {
    composable(
        route = NAVIGATION_SEARCH_REVIEW,
    ) {
        SearchReview(
            onBackPressed = onBackPressed,
            onReviewDetailClick = onReviewDetailClick,
        )
    }
}

/**
 * Navigates to the search review screen.
 *
 * Uses the route identified by [NAVIGATION_SEARCH_REVIEW].
 */
fun NavController.navigateToSearchReview() {
    navigate(NAVIGATION_SEARCH_REVIEW)
}