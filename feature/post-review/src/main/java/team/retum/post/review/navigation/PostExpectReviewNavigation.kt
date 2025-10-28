package team.retum.post.review.navigation

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

/**
 * Registers the "postExpectReview" navigation destination and hosts the PostExpectReview composable,
 * supplying it with the deserialized review data and the provided callbacks.
 *
 * @param onBackPressed Callback invoked when the user requests navigation back from the PostExpectReview screen.
 * @param onPostReviewCompleteClick Callback invoked when the user completes posting the review.
 */
fun NavGraphBuilder.postExpectReview(
    onBackPressed: () -> Unit,
    onPostReviewCompleteClick: () -> Unit,
) {
    composable(
        route = "$NAVIGATION_POST_EXPECT_REVIEW/{${ResourceKeys.REVIEW_DATA}}",
        arguments = listOf(navArgument(ResourceKeys.REVIEW_DATA) { NavType.StringType }),
    ) {
        PostExpectReview(
            reviewData = it.getReviewData(),
            onBackPressed = onBackPressed,
            onPostReviewCompleteClick = onPostReviewCompleteClick,
        )
    }
}

/**
 * Navigates to the Post Expect Review destination using the provided review data.
 *
 * The `reviewData` is serialized and embedded in the destination route so the target
 * destination can deserialize and use it.
 *
 * @param reviewData The post review data to pass to the destination.
fun NavController.navigateToPostExpectReview(reviewData: PostReviewData) {
    navigate("$NAVIGATION_POST_EXPECT_REVIEW/${reviewData.toJsonString()}")
}