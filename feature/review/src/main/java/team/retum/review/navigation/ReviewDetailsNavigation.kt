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
        route = "$NAVIGATION_REVIEW_DETAILS/{${ResourceKeys.REVIEW_ID}}/{$WRITER}",
        arguments = listOf(
            navArgument(ResourceKeys.REVIEW_ID) { NavType.StringType },
            navArgument(WRITER) { NavType.StringType },
        ),
    ) {
        val reviewId = it.arguments?.getString(ResourceKeys.REVIEW_ID) ?: ""
        val writer = it.arguments?.getString(WRITER) ?: ""
        ReviewDetails(
            onBackPressed = onBackPressed,
            writer = writer,
            reviewId = reviewId,
        )
    }
}

fun NavController.navigateToReviewDetails(
    reviewId: String,
    writer: String,
) {
    navigate("$NAVIGATION_REVIEW_DETAILS/$reviewId/$writer")
}
