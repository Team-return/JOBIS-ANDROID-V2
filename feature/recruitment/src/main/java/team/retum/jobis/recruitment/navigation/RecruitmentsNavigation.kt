package team.retum.jobis.recruitment.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import team.retum.jobis.recruitment.ui.Recruitments

const val NAVIGATION_RECRUITMENTS = "recruitments"

fun NavGraphBuilder.recruitments(
    onRecruitmentDetailsClick: (Long) -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: () -> Unit,
) {
    composable(NAVIGATION_RECRUITMENTS) {
        Recruitments(
            onRecruitmentDetailsClick = onRecruitmentDetailsClick,
            onRecruitmentFilterClick = onRecruitmentFilterClick,
            onSearchRecruitmentClick = onSearchRecruitmentClick,
        )
    }
}

fun NavHostController.navigateToRecruitments() {
    navigate(NAVIGATION_RECRUITMENTS)
}
