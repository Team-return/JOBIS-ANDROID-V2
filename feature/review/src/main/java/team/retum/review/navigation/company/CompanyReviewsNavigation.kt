package team.retum.review.navigation.company

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.review.ui.company.Reviews

const val NAVIGATION_COMPANY_REVIEWS = "companyReviews"

fun NavGraphBuilder.companyReviews(
    onBackPressed: () -> Unit,
    navigateToReviewDetails: (Long) -> Unit,
) {
    composable(
        route = "$NAVIGATION_COMPANY_REVIEWS/{${ResourceKeys.COMPANY_ID}}/{${ResourceKeys.COMPANY_NAME}}",
        arguments = listOf(
            navArgument(ResourceKeys.COMPANY_ID) { NavType.StringType },
            navArgument(ResourceKeys.COMPANY_NAME) { NavType.StringType },
        ),
    ) {
        val companyId = it.arguments?.getString(ResourceKeys.COMPANY_ID) ?: "0"
        val companyName = it.arguments?.getString(ResourceKeys.COMPANY_NAME) ?: ""
        Reviews(
            onBackPressed = onBackPressed,
            companyId = companyId.toLong(),
            companyName = companyName,
            navigateToReviewDetails = navigateToReviewDetails,
        )
    }
}

fun NavController.navigateToCompanyReviews(
    companyId: String,
    companyName: String,
) {
    navigate("$NAVIGATION_COMPANY_REVIEWS/$companyId/$companyName")
}
