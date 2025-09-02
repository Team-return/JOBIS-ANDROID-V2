package team.retum.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
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

fun NavController.navigateToReviews(
    companyId: Long,
    companyName: String,
) {
    navigate(NAVIGATION_REVIEWS)
}
