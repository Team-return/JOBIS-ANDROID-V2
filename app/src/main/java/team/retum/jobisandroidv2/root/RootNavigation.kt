package team.retum.jobisandroidv2.root

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.model.ApplicationData

const val NAVIGATION_ROOT = "root"
const val APPLICATION_ID = "applicationId"
const val INITIAL_TAB = "initialTab"

fun NavGraphBuilder.root(
    onAlarmClick: () -> Unit,
    onEmploymentClick: () -> Unit,
    onWinterInternClick: () -> Unit,
    onRecruitmentDetailsClick: (Long) -> Unit,
    onCompaniesClick: () -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: (Boolean) -> Unit,
    onNotificationSettingClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onSelectInterestClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onReportBugClick: () -> Unit,
    navigateToLanding: () -> Unit,
    onPostReviewClick: (String, Long) -> Unit,
    onReviewFilterClick: () -> Unit,
    onSearchReviewClick: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
    navigateToApplication: (ApplicationData) -> Unit,
    navigateToRecruitmentDetails: (Long) -> Unit,
    navigatedFromNotifications: Boolean,
) {
    composable(
        route = "$NAVIGATION_ROOT/{$APPLICATION_ID}?$INITIAL_TAB={$INITIAL_TAB}",
        arguments = listOf(
            navArgument(APPLICATION_ID) {
                type = NavType.LongType
            },
            navArgument(INITIAL_TAB) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
        ),
    ) {
        Root(
            initialTab = it.arguments?.getString(INITIAL_TAB),
            applicationId = it.arguments?.getLong(APPLICATION_ID),
            onAlarmClick = onAlarmClick,
            onEmploymentClick = onEmploymentClick,
            onWinterInternClick = onWinterInternClick,
            onRecruitmentDetailsClick = onRecruitmentDetailsClick,
            onCompaniesClick = onCompaniesClick,
            onRecruitmentFilterClick = onRecruitmentFilterClick,
            onSearchRecruitmentClick = onSearchRecruitmentClick,
            onNotificationSettingClick = onNotificationSettingClick,
            onNoticeClick = onNoticeClick,
            onSelectInterestClick = onSelectInterestClick,
            onChangePasswordClick = onChangePasswordClick,
            onReportBugClick = onReportBugClick,
            navigateToLanding = navigateToLanding,
            onReviewFilterClick = onReviewFilterClick,
            onSearchReviewClick = onSearchReviewClick,
            onPostReviewClick = onPostReviewClick,
            onReviewDetailClick = onReviewDetailClick,
            navigateToApplication = navigateToApplication,
            navigateToRecruitmentDetails = navigateToRecruitmentDetails,
            navigatedFromNotifications = navigatedFromNotifications,
        )
    }
}

fun NavController.navigateToRoot(applicationId: Long = 0, initialTab: String? = null) {
    val route = if (initialTab != null) {
        "$NAVIGATION_ROOT/$applicationId?$INITIAL_TAB=$initialTab"
    } else {
        "$NAVIGATION_ROOT/$applicationId"
    }
    navigate(route) {
        popUpTo(0)
    }
}
