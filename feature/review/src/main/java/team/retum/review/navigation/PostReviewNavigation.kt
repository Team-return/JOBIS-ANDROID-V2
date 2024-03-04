package team.retum.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.review.ui.PostReviewScreen

const val NAVIGATION_POST_REVIEW = "postReview"

fun NavGraphBuilder.postReview(onBackPressed: () -> Unit) {
    composable(NAVIGATION_POST_REVIEW) {
        PostReviewScreen(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToPostReview() {
    navigate(NAVIGATION_POST_REVIEW)
}
