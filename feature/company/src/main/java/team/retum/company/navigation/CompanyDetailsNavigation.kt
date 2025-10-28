package team.retum.company.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.company.ui.CompanyDetails

const val NAVIGATION_COMPANY_DETAILS = "companyDetails"

/**
 * Registers the Company Details destination on the NavGraphBuilder and forwards extracted route
 * arguments to the CompanyDetails composable.
 *
 * This sets up the route "companyDetails/{COMPANY_ID}/{IS_MOVED_RECRUITMENT_DETAILS}" and passes the
 * parsed `companyId` and `isMovedRecruitmentDetails` to the composable.
 *
 * @param onBackPressed Callback invoked when the user requests to navigate back.
 * @param navigateToReviewDetails Callback invoked with the companyId to navigate to a specific review's details.
 * @param navigateToReviews Callback invoked with the companyId and an additional String value used by the reviews destination.
 * @param navigateToRecruitmentDetails Callback invoked with the companyId and a Boolean flag indicating navigation context for recruitment details.
 */
fun NavGraphBuilder.companyDetails(
    onBackPressed: () -> Unit,
    navigateToReviewDetails: (Long) -> Unit,
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