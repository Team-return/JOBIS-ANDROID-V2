package team.retum.jobisandroidv2.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import team.retum.alarm.navigation.alarm
import team.retum.alarm.navigation.navigateToAlarm
import team.retum.bug.reportBug
import team.retum.company.navigation.companies
import team.retum.company.navigation.navigateToCompanies
import team.retum.company.navigation.navigateToSearchCompanies
import team.retum.company.navigation.searchCompanies
import team.retum.jobis.interests.navigation.interests
import team.retum.jobisandroidv2.root.NAVIGATION_ROOT
import team.retum.jobisandroidv2.root.root
import team.retum.recruitment.navigation.navigateToRecruitmentDetails
import team.retum.recruitment.navigation.recruitmentDetails

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
        )
        alarm(onBackPressed = navController::popBackStack)
        recruitmentDetails(onBackPressed = navController::navigateUp)
        reportBug()
        interests()
        companies(
            onBackPressed = navController::popBackStack,
            onSearchClick = navController::navigateToSearchCompanies,
        )
        searchCompanies(onBackPressed = navController::popBackStack)
    }
}
