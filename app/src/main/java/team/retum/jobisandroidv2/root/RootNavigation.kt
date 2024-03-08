package team.retum.jobisandroidv2.root

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.splash.navigation.NAVIGATION_SPLASH
import team.retum.landing.navigation.NAVIGATION_LANDING

const val NAVIGATION_ROOT = "root"

fun NavGraphBuilder.root(
    onAlarmClick: () -> Unit,
    onRecruitmentDetailsClick: (Long) -> Unit,
    onCompaniesClick: () -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onSelectInterestClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onReportBugClick: () -> Unit,
    navigateToLanding: () -> Unit,
    onPostReviewClick: (Long) -> Unit,
) {
    composable(NAVIGATION_ROOT) {
        Root(
            onAlarmClick = onAlarmClick,
            onRecruitmentDetailsClick = onRecruitmentDetailsClick,
            onCompaniesClick = onCompaniesClick,
            onRecruitmentFilterClick = onRecruitmentFilterClick,
            onSearchRecruitmentClick = onSearchRecruitmentClick,
            onNoticeClick = onNoticeClick,
            onSelectInterestClick = onSelectInterestClick,
            onChangePasswordClick = onChangePasswordClick,
            onReportBugClick = onReportBugClick,
            navigateToLanding = navigateToLanding,
            onPostReviewClick = onPostReviewClick,
        )
    }
}

fun NavController.navigateToRoot() {
    navigate(NAVIGATION_ROOT) {
        popUpTo(NAVIGATION_SPLASH) {
            inclusive = true
        }
    }
}
