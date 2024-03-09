package team.retum.jobis.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.jobis.recruitment.ui.RecruitmentDetails

const val NAVIGATION_RECRUITMENT_DETAILS = "recruitmentDetails/"
const val RECRUITMENT_ID = "recruitment-id"

fun NavGraphBuilder.recruitmentDetails(
    onBackPressed: () -> Unit,
    onApplyClick: (Long, String, String) -> Unit,
) {
    composable(
        route = "$NAVIGATION_RECRUITMENT_DETAILS{$RECRUITMENT_ID}",
        arguments = listOf(navArgument(RECRUITMENT_ID) { NavType.StringType }),
    ) {
        RecruitmentDetails(
            recruitmentId = it.arguments?.getString(RECRUITMENT_ID)?.toLong()
                ?: throw NullPointerException(),
            onBackPressed = onBackPressed,
            onApplyClick = onApplyClick,
        )
    }
}

fun NavController.navigateToRecruitmentDetails(recruitmentId: Long) {
    navigate(NAVIGATION_RECRUITMENT_DETAILS + recruitmentId)
}
