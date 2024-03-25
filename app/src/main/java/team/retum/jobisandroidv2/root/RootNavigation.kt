package team.retum.jobisandroidv2.root

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.model.ApplicationData
import team.retum.jobis.splash.navigation.NAVIGATION_SPLASH

const val NAVIGATION_ROOT = "root"
const val APPLICATION_ID = "applicationId"

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
    navigateToApplication: (ApplicationData) -> Unit,
    navigateToRecruitmentDetails: (Long) -> Unit,
    navigatedFromNotifications: Boolean,
) {
    composable(
        route = "$NAVIGATION_ROOT{$APPLICATION_ID}",
        arguments = listOf(navArgument(APPLICATION_ID) { NavType.StringType }),
    ) {
        Root(
            applicationId = it.arguments?.getString(APPLICATION_ID)?.toLong(),
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
            navigateToApplication = navigateToApplication,
            navigateToRecruitmentDetails = navigateToRecruitmentDetails,
            navigatedFromNotifications = navigatedFromNotifications,
        )
    }
}

fun NavController.navigateToRoot(applicationId: Long = 0) {
    navigate(NAVIGATION_ROOT + applicationId) {
        popUpTo(0)
    }
}
