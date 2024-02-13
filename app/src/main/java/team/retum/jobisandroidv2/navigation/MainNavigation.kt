package team.retum.jobisandroidv2.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import team.retum.alarm.navigation.alarm
import team.retum.alarm.navigation.navigateToAlarm
import team.retum.jobisandroidv2.root.NAVIGATION_ROOT
import team.retum.jobisandroidv2.root.root
import team.retum.recruitment.navigation.navigateToRecruitmentDetail
import team.retum.recruitment.navigation.recruitmentDetail
import team.retum.recruitment.navigation.recruitments

const val NAVIGATION_MAIN = "main"

fun NavGraphBuilder.mainNavigation(navController: NavHostController) {
    navigation(
        route = NAVIGATION_MAIN,
        startDestination = NAVIGATION_ROOT,
    ) {
        root(
            onAlarmClick = navController::navigateToAlarm,
            onRecruitmentDetailClick = navController::navigateToRecruitmentDetail,
        )
        alarm(onBackPressed = navController::popBackStack)
        recruitmentDetail(onBackPressed = navController::navigateUp)
    }
}
