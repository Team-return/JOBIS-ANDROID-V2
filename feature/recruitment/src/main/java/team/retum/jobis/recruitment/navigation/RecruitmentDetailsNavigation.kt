package team.retum.jobis.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.recruitment.ui.RecruitmentDetails

const val NAVIGATION_RECRUITMENT_DETAILS = "recruitmentDetails/"
const val RECRUITMENT_ID = "{recruitment-id}"

fun NavGraphBuilder.recruitmentDetails(
    onBackPressed: () -> Unit,
    onApplyClick: () -> Unit,
) {
    composable(NAVIGATION_RECRUITMENT_DETAILS + RECRUITMENT_ID) {
        RecruitmentDetails(
            onBackPressed = onBackPressed,
            onApplyClick = onApplyClick,
        )
    }
}

fun NavController.navigateToRecruitmentDetails(recruitmentId: Long) {
    navigate(NAVIGATION_RECRUITMENT_DETAILS + recruitmentId)
}
