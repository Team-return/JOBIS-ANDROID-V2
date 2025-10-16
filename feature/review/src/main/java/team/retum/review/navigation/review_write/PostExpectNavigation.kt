package team.retum.review.navigation.review_write

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.review.ui.review_write.PostExpectReview

const val NAVIGATION_POST_EXPECT_REVIEW = "post_expect_reviews"

fun NavGraphBuilder.postExpectReview(
    onBackPressed: () -> Unit,
) {
    composable(
        route = NAVIGATION_POST_EXPECT_REVIEW,
    ) {
        PostExpectReview(
            onBackPressed = onBackPressed,
        )
    }
}

fun NavController.navigateToPostExpectReview() {
    navigate(NAVIGATION_POST_EXPECT_REVIEW)
}
