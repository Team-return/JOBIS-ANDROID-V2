package team.retum.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.review.ui.Review

const val NAVIGATION_REVIEW = "review"

/**
 * Registers the Review screen route on this NavGraphBuilder and binds navigation callbacks.
 *
 * @param onReviewFilterClick Invoked when the review filter action is triggered.
 * @param onSearchReviewClick Invoked when the review search action is triggered.
 * @param onReviewDetailClick Invoked with the selected review's ID when a review item is opened for details.
 */
fun NavGraphBuilder.review(
    onReviewFilterClick: () -> Unit,
    onSearchReviewClick: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
) {
    composable(
        route = NAVIGATION_REVIEW,
    ) {
        Review(
            onReviewFilterClick = onReviewFilterClick,
            onSearchReviewClick = onSearchReviewClick,
            onReviewDetailClick = onReviewDetailClick,
        )
    }
}

/**
 * Navigates to the Review screen route.
 */
fun NavController.navigateToReview() {
    navigate(NAVIGATION_REVIEW)
}