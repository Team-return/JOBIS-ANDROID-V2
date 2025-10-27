package team.retum.post_review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.post_review.ui.PostReviewComplete

const val NAVIGATION_POST_REVIEW_COMPLETE = "postReviewComplete"

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

fun NavController.navigateToPostReviewComplete() {
    navigate(NAVIGATION_POST_REVIEW_COMPLETE)
}
