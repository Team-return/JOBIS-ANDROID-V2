package team.retum.post.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.post.review.ui.PostReviewComplete

const val NAVIGATION_POST_REVIEW_COMPLETE = "postReviewComplete"

/**
 * Registers a composable destination for the Post Review Complete screen.
 *
 * @param onBackPressed Callback invoked when the screen requests back navigation.
 * @param navigateToPostReview Callback invoked to navigate to the post review screen; receives the post identifier and an additional numeric parameter required for navigation.
 */
fun NavGraphBuilder.postReviewComplete(
    onBackPressed: () -> Unit,
    navigateToPostReview: (String, Long) -> Unit,
) {
    composable(
        route = NAVIGATION_POST_REVIEW_COMPLETE,
    ) {
        PostReviewComplete(
            onBackPressed = onBackPressed,
            navigateToPostReview = navigateToPostReview,
        )
    }
}

/**
 * Navigate to the Post Review Complete screen.
 */
fun NavController.navigateToPostReviewComplete() {
    navigate(NAVIGATION_POST_REVIEW_COMPLETE)
}