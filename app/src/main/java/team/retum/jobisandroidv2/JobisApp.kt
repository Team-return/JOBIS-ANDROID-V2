package team.retum.jobisandroidv2

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import team.retum.jobisandroidv2.navigation.NAVIGATION_AUTH
import team.retum.jobisandroidv2.navigation.authNavigation
import team.retum.jobisandroidv2.navigation.mainNavigation

@Composable
internal fun JobisApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NAVIGATION_AUTH,
    ) {
        authNavigation()
        mainNavigation()
    }
}
