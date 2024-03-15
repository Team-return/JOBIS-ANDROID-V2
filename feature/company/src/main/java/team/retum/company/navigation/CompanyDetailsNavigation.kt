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
    navigateToRecruitmentDetails: (Long, Boolean) -> Unit,
) {
    composable(
        route = "$NAVIGATION_COMPANY_DETAILS/{${ResourceKeys.COMPANY_ID}}/{${ResourceKeys.IS_MOVED_RECRUITMENT_DETAILS}}",
        arguments = listOf(
            navArgument(ResourceKeys.COMPANY_ID) { type = NavType.LongType },
            navArgument(ResourceKeys.IS_MOVED_RECRUITMENT_DETAILS) { type = NavType.BoolType },
        ),
    ) {
        val companyId = it.arguments?.getLong(ResourceKeys.COMPANY_ID) ?: 0L
        val isMovedRecruitmentDetails =
            it.arguments?.getBoolean(ResourceKeys.IS_MOVED_RECRUITMENT_DETAILS) ?: false
        CompanyDetails(
            companyId = companyId,
            onBackPressed = onBackPressed,
            navigateToReviewDetails = navigateToReviewDetails,
            navigateToReviews = navigateToReviews,
            navigateToRecruitmentDetails = navigateToRecruitmentDetails,
            isMovedRecruitmentDetails = isMovedRecruitmentDetails,
        )
    }
}

fun NavController.navigateToCompanyDetails(
    companyId: Long,
    isMovedRecruitmentDetails: Boolean = false,
) {
    navigate("$NAVIGATION_COMPANY_DETAILS/$companyId/$isMovedRecruitmentDetails")
}
