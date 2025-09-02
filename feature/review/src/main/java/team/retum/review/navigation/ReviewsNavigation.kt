package team.retum.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.review.ui.Reviews

const val NAVIGATION_REVIEWS = "reviews"

fun NavGraphBuilder.reviews(
    onBackPressed: () -> Unit,
    navigateToReviewDetails: () -> Unit,
) {
    composable(
        route = NAVIGATION_REVIEWS,
    ) {
        Reviews()
    }
}

fun NavController.navigateToReviews() {
    navigate(NAVIGATION_REVIEWS)
}
