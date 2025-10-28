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

fun NavController.navigateToPostNextReview(reviewData: PostReviewData) {
    navigate("$NAVIGATION_POST_NEXT_REVIEW/${reviewData.toJsonString()}")
}

internal fun NavBackStackEntry.getReviewData(): PostReviewData {
    val reviewData = arguments?.getString(ResourceKeys.REVIEW_DATA) ?: throw NullPointerException()
    return reviewData.toReviewData()
}
