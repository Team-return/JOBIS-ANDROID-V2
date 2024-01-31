package team.retum.jobisandroidv2.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import team.retum.jobisandroidv2.ui.NAVIGATION_ROOT
import team.retum.jobisandroidv2.ui.RootScreen

const val NAVIGATION_MAIN = "main"

fun NavGraphBuilder.mainNavigation() {
    navigation(
        route = NAVIGATION_MAIN,
        startDestination = NAVIGATION_ROOT,
    ) {
        composable(NAVIGATION_ROOT) {
            RootScreen()
        }
    }
}
