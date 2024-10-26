package team.retum.jobis.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.recruitment.ui.WinterIntern

private const val NAVIGATION_WINTER_INTERN = "winterIntern"

fun NavGraphBuilder.winterIntern(
    onBackPressed: () -> Unit,
    onRecruitmentDetailsClick: (Long) -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: (Boolean) -> Unit,
) {
    composable(NAVIGATION_WINTER_INTERN) {
        WinterIntern(
            onBackPressed = onBackPressed,
            onRecruitmentDetailsClick = onRecruitmentDetailsClick,
            onRecruitmentFilterClick = onRecruitmentFilterClick,
            onSearchRecruitmentClick = onSearchRecruitmentClick,
        )
    }
}

fun NavController.navigateToWinterIntern() {
    navigate(NAVIGATION_WINTER_INTERN)
}
