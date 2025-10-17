package team.retum.review.navigation.review_write

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.review.model.PostReviewData
import team.retum.review.model.toJsonString
import team.retum.review.model.toReviewData
import team.retum.review.ui.review_write.PostNextReview

const val NAVIGATION_POST_NEXT_REVIEW = "post_next_reviews"

fun NavGraphBuilder.postNextReview(
    onBackPressed: () -> Unit,
    onPostExpectReviewClick: () -> Unit,
) {
    composable(
        route = "$NAVIGATION_POST_NEXT_REVIEW/{${ResourceKeys.REVIEW_DATA}}",
        arguments = listOf(navArgument(ResourceKeys.REVIEW_DATA) { NavType.StringType }),
    ) {

        PostNextReview(
            reviewData = it.getReviewData(),
            onBackPressed = onBackPressed,
            onPostExpectReviewClick = onPostExpectReviewClick,
        )
    }
}

fun NavController.navigateToPostNextReview(reviewData: PostReviewData) {
    navigate("$NAVIGATION_POST_NEXT_REVIEW/${reviewData.toJsonString()}")
}

internal fun NavBackStackEntry.getReviewData(): PostReviewData {
    val reviewData = arguments?.getString(ResourceKeys.REVIEW_DATA) ?: throw NullPointerException()
    return reviewData.toReviewData()
}
