package team.retum.jobisandroidv2.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import team.retum.alarm.navigation.alarm
import team.retum.jobisandroidv2.root.NAVIGATION_ROOT
import team.retum.jobisandroidv2.root.root

const val NAVIGATION_MAIN = "main"

fun NavGraphBuilder.mainNavigation(navHostController: NavHostController) {
    navigation(
        route = NAVIGATION_MAIN,
        startDestination = NAVIGATION_ROOT,
    ) {
        root(navHostController = navHostController)
        alarm(onBackPressed = navHostController::popBackStack)
    }
}
