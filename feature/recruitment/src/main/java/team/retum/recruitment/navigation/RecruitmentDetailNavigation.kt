package team.retum.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.recruitment.ui.RecruitmentDetails

const val NAVIGATION_RECRUITMENTS_DETAIL = "recruitmentDetail"

fun NavGraphBuilder.recruitmentDetail(
    onBackPressed: () -> Unit,
) {
    composable(NAVIGATION_RECRUITMENTS_DETAIL) {
        RecruitmentDetails(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToRecruitmentDetail() {
    navigate(NAVIGATION_RECRUITMENTS_DETAIL)
}