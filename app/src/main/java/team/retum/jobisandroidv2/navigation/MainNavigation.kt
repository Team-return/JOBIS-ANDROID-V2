package team.retum.jobisandroidv2.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import team.retum.alarm.navigation.alarm
import team.retum.jobisandroidv2.root.NAVIGATION_ROOT
import team.retum.jobisandroidv2.root.root

const val NAVIGATION_MAIN = "main"

fun NavGraphBuilder.mainNavigation(navController: NavController) {
    navigation(
        route = NAVIGATION_MAIN,
        startDestination = NAVIGATION_ROOT,
    ) {
        root(navHostController = navController)
        alarm(onBackPressed = navController::popBackStack)
    }
}
