package team.retum.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.review.ui.ReviewDetails

const val NAVIGATION_REVIEW_DETAILS = "reviewDetails"

/**
 * Registers the Review Details destination on the navigation graph.
 *
 * Adds a composable route "reviewDetails/{REVIEW_ID}" that accepts a Long `REVIEW_ID` argument
 * and displays the ReviewDetails screen for that ID.
 *
 * @param onBackPressed Invoked when the user requests to navigate back from the ReviewDetails screen.
 */
fun NavGraphBuilder.reviewDetails(
    onBackPressed: () -> Unit,
) {
    composable(
        route = "$NAVIGATION_REVIEW_DETAILS/{${ResourceKeys.REVIEW_ID}}",
        arguments = listOf(
            navArgument(ResourceKeys.REVIEW_ID) { type = NavType.LongType },
        ),
    ) {
        val reviewId = it.arguments?.getLong(ResourceKeys.REVIEW_ID) ?: 0

        ReviewDetails(
            reviewId = reviewId,
            onBackPressed = onBackPressed,
        )
    }
}

/**
 * Navigate to the Review Details screen for the given review.
 *
 * @param reviewId The numeric identifier of the review to open.
 */
fun NavController.navigateToReviewDetails(reviewId: Long) {
    navigate("$NAVIGATION_REVIEW_DETAILS/$reviewId")
}