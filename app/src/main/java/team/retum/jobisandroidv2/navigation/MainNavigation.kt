package team.retum.jobisandroidv2.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import team.retum.alarm.navigation.alarm
import team.retum.alarm.navigation.navigateToAlarm
import team.retum.bug.navigateToReportBug
import team.retum.bug.reportBug
import team.retum.company.navigation.companies
import team.retum.company.navigation.navigateToCompanies
import team.retum.company.navigation.navigateToSearchCompanies
import team.retum.company.navigation.searchCompanies
import team.retum.jobis.change_password.navigation.navigateToConfirmPassword
import team.retum.jobis.interests.navigation.interests
import team.retum.jobis.interests.navigation.navigateToInterests
import team.retum.jobis.notification.navigation.navigateToNotificationsList
import team.retum.jobis.notification.navigation.notificationDetails
import team.retum.jobis.notification.navigation.notificationList
import team.retum.jobis.recruitment.navigation.navigateToRecruitmentDetails
import team.retum.jobis.recruitment.navigation.navigateToRecruitmentFilter
import team.retum.jobis.recruitment.navigation.navigateToSearchRecruitment
import team.retum.jobis.recruitment.navigation.recruitmentDetails
import team.retum.jobis.recruitment.navigation.recruitmentFilter
import team.retum.jobis.recruitment.navigation.searchRecruitment
import team.retum.jobisandroidv2.application
import team.retum.jobisandroidv2.navigateToApplication
import team.retum.jobisandroidv2.root.NAVIGATION_ROOT
import team.retum.jobisandroidv2.root.root

const val NAVIGATION_MAIN = "main"

fun NavGraphBuilder.mainNavigation(navController: NavHostController) {
    navigation(
        route = NAVIGATION_MAIN,
        startDestination = NAVIGATION_ROOT,
    ) {
        root(
            onAlarmClick = navController::navigateToAlarm,
            onRecruitmentDetailsClick = navController::navigateToRecruitmentDetails,
            onCompaniesClick = navController::navigateToCompanies,
            onRecruitmentFilterClick = navController::navigateToRecruitmentFilter,
            onSelectInterestClick = navController::navigateToInterests,
            onChangePasswordClick = navController::navigateToConfirmPassword,
            onReportBugClick = navController::navigateToReportBug,
            onSearchRecruitmentClick = navController::navigateToSearchRecruitment,
            onNoticeClick = navController::navigateToNotificationsList,
        )
        alarm(onBackPressed = navController::popBackStack)
        recruitmentDetails(
            onBackPressed = navController::navigateUp,
            onApplyClick = navController::navigateToApplication,
        )
        reportBug(onBackPressed = navController::popBackStack)
        interests(onBackPressed = navController::popBackStack)
        notificationDetails(onBackPressed = navController::navigateUp)
        companies(
            onBackPressed = navController::popBackStack,
            onSearchClick = navController::navigateToSearchCompanies,
        )
        searchCompanies(onBackPressed = navController::popBackStack)
        recruitmentFilter(onBackPressed = navController::popBackStack)
        searchRecruitment(
            onBackPressed = navController::popBackStack,
            onRecruitmentDetailsClick = navController::navigateToRecruitmentDetails,
        )
        application(onBackPressed = navController::popBackStack)
        notificationList(onBackPressed = navController::popBackStack)
    }
}
