package team.retum.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.review.ui.ReviewDetails

const val NAVIGATION_REVIEW_DETAILS = "reviewDetails"
const val WRITER = "writer"

fun NavGraphBuilder.reviewDetails(
    onBackPressed: () -> Unit,
) {
    composable(
        route = NAVIGATION_REVIEW_DETAILS,
    ) {
        ReviewDetails(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToReviewDetails() {
    navigate(NAVIGATION_REVIEW_DETAILS)
}
