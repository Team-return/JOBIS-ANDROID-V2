package team.retum.company.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.company.ui.CompanyDetails

const val NAVIGATION_COMPANY_DETAILS = "companyDetails"

fun NavGraphBuilder.companyDetails(
    onBackPressed: () -> Unit,
    navigateToReviewDetails: (String, String) -> Unit,
    navigateToReviews: (Long, String) -> Unit,
) {
    composable(
        route = "$NAVIGATION_COMPANY_DETAILS/{${ResourceKeys.COMPANY_ID}}",
        arguments = listOf(navArgument(ResourceKeys.COMPANY_ID) { NavType.StringType }),
    ) {
        val companyId =
            it.arguments?.getString(ResourceKeys.COMPANY_ID) ?: throw NullPointerException()
        CompanyDetails(
            companyId = companyId.toLong(),
            onBackPressed = onBackPressed,
            navigateToReviewDetails = navigateToReviewDetails,
            navigateToReviews = navigateToReviews,
        )
    }
}

fun NavController.navigateToCompanyDetails(companyId: Long) {
    navigate("$NAVIGATION_COMPANY_DETAILS/$companyId")
}
