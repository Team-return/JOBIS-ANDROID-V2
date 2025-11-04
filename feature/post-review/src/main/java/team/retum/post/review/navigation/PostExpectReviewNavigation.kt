package team.retum.post.review.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.post.review.model.PostReviewData
import team.retum.post.review.model.toJsonString
import team.retum.post.review.ui.PostExpectReview

const val NAVIGATION_POST_EXPECT_REVIEW = "postExpectReview"

fun NavGraphBuilder.postExpectReview(
    onBackPressed: () -> Unit,
    onPostReviewCompleteClick: () -> Unit,
) {
    composable(
        route = "$NAVIGATION_POST_EXPECT_REVIEW/{${ResourceKeys.REVIEW_DATA}}",
        arguments = listOf(navArgument(ResourceKeys.REVIEW_DATA) { type = NavType.StringType }),
    ) {
        PostExpectReview(
            reviewData = it.getReviewData(),
            onBackPressed = onBackPressed,
            onPostReviewCompleteClick = onPostReviewCompleteClick,
        )
    }
}

fun NavController.navigateToPostExpectReview(reviewData: PostReviewData) {
    navigate("$NAVIGATION_POST_EXPECT_REVIEW/${Uri.encode(reviewData.toJsonString())}")
}
