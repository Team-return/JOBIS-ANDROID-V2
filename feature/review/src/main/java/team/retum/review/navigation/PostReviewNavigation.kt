package team.retum.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.review.ui.PostReview

const val NAVIGATION_POST_REVIEW = "postReview"

private const val COMPANY_ID = "companyId"
fun NavGraphBuilder.postReview(onBackPressed: () -> Unit) {
    composable(
        route = "$NAVIGATION_POST_REVIEW/{$COMPANY_ID}",
        arguments = listOf(navArgument(COMPANY_ID) { NavType.StringType }),
    ) {
        PostReview(
            onBackPressed = onBackPressed,
            companyId = it.arguments?.getString(COMPANY_ID)?.toLong()
                ?: throw NullPointerException(),
        )
    }
}

fun NavController.navigateToPostReview(companyId: Long) {
    navigate("$NAVIGATION_POST_REVIEW/$companyId")
}
