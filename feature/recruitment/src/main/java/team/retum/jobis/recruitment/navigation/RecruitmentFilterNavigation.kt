package team.retum.jobis.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.recruitment.ui.RecruitmentFilter

const val NAVIGATION_RECRUITMENT_FILTER = "recruitmentFilter"

fun NavGraphBuilder.recruitmentFilter(onBackPressed: () -> Unit) {
    composable(NAVIGATION_RECRUITMENT_FILTER) {
        RecruitmentFilter(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToRecruitmentFilter() {
    navigate(NAVIGATION_RECRUITMENT_FILTER)
}
