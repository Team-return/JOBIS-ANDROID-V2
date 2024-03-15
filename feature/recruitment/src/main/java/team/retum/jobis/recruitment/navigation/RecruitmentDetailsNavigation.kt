package team.retum.jobis.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.model.ReApplyData
import team.retum.common.utils.ResourceKeys
import team.retum.jobis.recruitment.ui.RecruitmentDetails

const val NAVIGATION_RECRUITMENT_DETAILS = "recruitmentDetails"
private const val RECRUITMENT_ID = "recruitment-id"

fun NavGraphBuilder.recruitmentDetails(
    onBackPressed: () -> Unit,
    onApplyClick: (ReApplyData) -> Unit,
    navigateToCompanyDetails: (Long, Boolean) -> Unit,
) {
    composable(
        route = "$NAVIGATION_RECRUITMENT_DETAILS/{$RECRUITMENT_ID}/{${ResourceKeys.IS_MOVED_COMPANY_DETAILS}}",
        arguments = listOf(
            navArgument(RECRUITMENT_ID) { type = NavType.LongType },
            navArgument(ResourceKeys.IS_MOVED_COMPANY_DETAILS) { type = NavType.BoolType },
        ),
    ) {
        val recruitmentId = it.arguments?.getLong(RECRUITMENT_ID) ?: 0L
        val isMovedCompanyDetails = it.arguments?.getBoolean(ResourceKeys.IS_MOVED_COMPANY_DETAILS) ?: false
        RecruitmentDetails(
            recruitmentId = recruitmentId,
            onBackPressed = onBackPressed,
            onApplyClick = onApplyClick,
            navigateToCompanyDetails = navigateToCompanyDetails,
            isMovedCompanyDetails = isMovedCompanyDetails,
        )
    }
}

fun NavController.navigateToRecruitmentDetails(
    recruitmentId: Long,
    isMovedCompanyDetails: Boolean = false,
) {
    navigate("$NAVIGATION_RECRUITMENT_DETAILS/$recruitmentId/$isMovedCompanyDetails")
}
