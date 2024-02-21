package team.retum.jobis.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.recruitment.ui.SearchRecruitment

const val NAVIGATION_SEARCH_RECRUITMENT = "searchRecruitment"

fun NavGraphBuilder.searchRecruitment(
    onBackPressed: () -> Unit,
    onRecruitmentClick: (Long) -> Unit,
) {
    composable(NAVIGATION_SEARCH_RECRUITMENT) {
        SearchRecruitment(
            onBackPressed = onBackPressed,
            onRecruitmentClick = onRecruitmentClick,
        )
    }
}

fun NavController.navigateToSearchRecruitment() {
    navigate(NAVIGATION_SEARCH_RECRUITMENT)
}
