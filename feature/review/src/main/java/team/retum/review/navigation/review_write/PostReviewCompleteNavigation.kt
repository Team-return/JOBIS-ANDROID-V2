package team.retum.review.navigation.review_write

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.review.model.PostReviewData
import team.retum.review.ui.review_write.PostReview
import team.retum.review.ui.review_write.PostReviewComplete
import team.retum.review.ui.review_write.PostReviewCompleteScreen

const val NAVIGATION_POST_REVIEW_COMPLETE = "postReviewComplete"

fun NavGraphBuilder.postReviewComplete() {
    composable(
        route = NAVIGATION_POST_REVIEW_COMPLETE,
    ) {
        PostReviewComplete()
    }
}

fun NavController.navigateToPostReviewComplete() {
    navigate(NAVIGATION_POST_REVIEW_COMPLETE)
}
