package team.retum.jobisandroidv2.root

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val NAVIGATION_ROOT = "root"

fun NavGraphBuilder.root(navHostController: NavController) {
    composable(NAVIGATION_ROOT) {
        Root(navHostController = navHostController)
    }
}

fun NavController.navigateToRoot() {
    navigate(NAVIGATION_ROOT)
}
