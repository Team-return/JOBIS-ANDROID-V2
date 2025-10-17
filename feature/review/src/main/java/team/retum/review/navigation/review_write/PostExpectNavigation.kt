package team.retum.review.navigation.review_write

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.review.model.PostReviewData
import team.retum.review.model.toJsonString
import team.retum.review.ui.review_write.PostExpectReview

const val NAVIGATION_POST_EXPECT_REVIEW = "post_expect_reviews"

fun NavGraphBuilder.postExpectReview(
    onBackPressed: () -> Unit,
) {
    composable(
        route = "$NAVIGATION_POST_EXPECT_REVIEW/{${ResourceKeys.REVIEW_DATA}}",
        arguments = listOf(navArgument(ResourceKeys.REVIEW_DATA) { NavType.StringType }),
    ) {
        PostExpectReview(
            reviewData = it.getReviewData(),
            onBackPressed = onBackPressed,
        )
    }
}

fun NavController.navigateToPostExpectReview(reviewData: PostReviewData) {
    navigate("$NAVIGATION_POST_EXPECT_REVIEW/${reviewData.toJsonString()}")
}
