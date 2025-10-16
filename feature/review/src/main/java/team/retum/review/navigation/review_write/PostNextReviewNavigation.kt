package team.retum.review.navigation.review_write

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.review.navigation.review_read.NAVIGATION_REVIEWS
import team.retum.review.ui.review_read.Reviews
import team.retum.review.ui.review_write.PostNextReview

const val NAVIGATION_POST_NEXT_REVIEW = "post_next_reviews"

fun NavGraphBuilder.postNextReview(
    onBackPressed: () -> Unit,
    onPostExpectReviewClick: () -> Unit,
) {
    composable(
        route = NAVIGATION_POST_NEXT_REVIEW,
    ) {
        PostNextReview(
            onBackPressed = onBackPressed,
            onPostExpectReviewClick = onPostExpectReviewClick,
        )
    }
}

fun NavController.navigateToPostNextReview() {
    navigate(NAVIGATION_POST_NEXT_REVIEW)
}
