package team.retum.post.review.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.post.review.model.PostReviewData
import team.retum.post.review.model.toJsonString
import team.retum.post.review.model.toReviewData
import team.retum.post.review.ui.PostNextReview

const val NAVIGATION_POST_NEXT_REVIEW = "postNextReview"

/**
 * Registers the navigation destination for the "post next review" screen.
 *
 * Declares a composable route that requires serialized review data as a path argument, deserializes it, and renders the PostNextReview UI.
 *
 * @param onBackPressed Callback invoked when the screen requests to navigate back.
 * @param navigateToPostExpectReview Callback invoked with the deserialized PostReviewData to navigate to the post expect review flow.
 */
fun NavGraphBuilder.postNextReview(
    onBackPressed: () -> Unit,
    navigateToPostExpectReview: (PostReviewData) -> Unit,
) {
    composable(
        route = "$NAVIGATION_POST_NEXT_REVIEW/{${ResourceKeys.REVIEW_DATA}}",
        arguments = listOf(navArgument(ResourceKeys.REVIEW_DATA) { NavType.StringType }),
    ) {
        PostNextReview(
            reviewData = it.getReviewData(),
            onBackPressed = onBackPressed,
            navigateToPostExpectReview = navigateToPostExpectReview,
        )
    }
}

/**
 * Navigates to the Post Next Review screen using the given review data.
 *
 * @param reviewData The review data to provide to the destination screen.
 */
fun NavController.navigateToPostNextReview(reviewData: PostReviewData) {
    navigate("$NAVIGATION_POST_NEXT_REVIEW/${reviewData.toJsonString()}")
}

/**
 * Extracts the PostReviewData stored in this NavBackStackEntry's navigation arguments.
 *
 * Retrieves the string argument identified by ResourceKeys.REVIEW_DATA and converts it to a PostReviewData.
 *
 * @return The PostReviewData parsed from the REVIEW_DATA navigation argument.
 * @throws NullPointerException if the REVIEW_DATA argument is missing.
 */
internal fun NavBackStackEntry.getReviewData(): PostReviewData {
    val reviewData = arguments?.getString(ResourceKeys.REVIEW_DATA) ?: throw NullPointerException()
    return reviewData.toReviewData()
}