package team.retum.post.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.post.review.model.PostReviewData
import team.retum.post.review.ui.PostReview
import kotlin.text.toLongOrNull

const val NAVIGATION_POST_REVIEW = "postReview"

/**
 * Registers the "postReview" navigation destination and binds route arguments to the PostReview UI.
 *
 * The destination route includes `companyName` and `companyId` path parameters; when visited the
 * PostReview composable is shown with the extracted `companyName` (defaults to empty string if missing)
 * and `companyId` parsed to a `Long` (defaults to 0L if missing or invalid).
 *
 * @param onBackPressed Callback invoked when the PostReview UI requests a back navigation.
 * @param navigateToPostNextReview Callback invoked with PostReviewData to navigate to the next review step.
 */
fun NavGraphBuilder.postReview(
    onBackPressed: () -> Unit,
    navigateToPostNextReview: (PostReviewData) -> Unit,
) {
    composable(
        route = "$NAVIGATION_POST_REVIEW/{${ResourceKeys.COMPANY_NAME}}/{${ResourceKeys.COMPANY_ID}}",
        arguments = listOf(
            navArgument(ResourceKeys.COMPANY_NAME) { NavType.StringType },
            navArgument(ResourceKeys.COMPANY_ID) { NavType.StringType },
        ),
    ) {
        PostReview(
            onBackPressed = onBackPressed,
            navigateToPostNextReview = navigateToPostNextReview,
            companyName = it.arguments?.getString(ResourceKeys.COMPANY_NAME) ?: "",
            companyId = it.arguments?.getString(ResourceKeys.COMPANY_ID)?.toLongOrNull() ?: 0L,
        )
    }
}

/**
 * Navigate to the post-review screen for the specified company.
 *
 * @param companyName The company name included in the navigation route.
 * @param companyId The company ID included in the navigation route.
 */
fun NavController.navigateToPostReview(companyName: String, companyId: Long) {
    navigate("$NAVIGATION_POST_REVIEW/$companyName/$companyId")
}