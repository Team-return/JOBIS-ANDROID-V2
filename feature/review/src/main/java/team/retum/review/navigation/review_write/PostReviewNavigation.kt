package team.retum.review.navigation.review_write

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.review.model.PostReviewData
import team.retum.review.ui.review_write.PostReview

const val NAVIGATION_POST_REVIEW = "postReview"

fun NavGraphBuilder.postReview(
    onBackPressed: () -> Unit,
    navigateToPostNextReview: (PostReviewData) -> Unit,
) {
    composable(
        route = "$NAVIGATION_POST_REVIEW/{${ResourceKeys.COMPANY_NAME}}/{${ResourceKeys.COMPANY_ID}}",
        arguments = listOf(
            navArgument(ResourceKeys.COMPANY_NAME) { NavType.StringType },
            navArgument(ResourceKeys.COMPANY_ID) { NavType.LongType },
        ),
    ) {
        PostReview(
            onBackPressed = onBackPressed,
            navigateToPostNextReview = navigateToPostNextReview,
            companyName = it.arguments?.getString(ResourceKeys.COMPANY_NAME) ?: "",
            companyId = it.arguments?.getLong(ResourceKeys.COMPANY_ID)?.toLong() ?: 0L,
        )
    }
}

fun NavController.navigateToPostReview(companyName: String, companyId: Long) {
    navigate("$NAVIGATION_POST_REVIEW/$companyName/$companyId")
}
