package team.retum.review.navigation.review_write

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.review.model.PostReviewData
import team.retum.review.model.toReviewData
import team.retum.review.ui.review_write.PostReview

const val NAVIGATION_POST_REVIEW = "postReview"

fun NavGraphBuilder.postReview(
    onBackPressed: () -> Unit,
    navigateToPostNextReview: (PostReviewData) -> Unit,
) {
    composable(
        route = "$NAVIGATION_POST_REVIEW/{${ResourceKeys.COMPANY_NAME}}",
        arguments = listOf(navArgument(ResourceKeys.COMPANY_NAME) { NavType.StringType }),
    ) {
        PostReview(
            onBackPressed = onBackPressed,
            navigateToPostNextReview = navigateToPostNextReview,
            companyName = it.arguments?.getString(ResourceKeys.COMPANY_NAME)!!,
        )
    }
}

fun NavController.navigateToPostReview(companyName: String) {
    navigate("$NAVIGATION_POST_REVIEW/$companyName")
}
