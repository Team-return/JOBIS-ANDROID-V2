package team.retum.jobisandroidv2.ui

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import team.retum.jobisandroidv2.navigation.NAVIGATION_AUTH
import team.retum.jobisandroidv2.navigation.NAVIGATION_MAIN
import team.retum.jobisandroidv2.navigation.authNavigation
import team.retum.jobisandroidv2.navigation.mainNavigation

@Composable
internal fun JobisApp() {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier
            .navigationBarsPadding()
            .statusBarsPadding(),
        navController = navController,
        startDestination = NAVIGATION_MAIN,
    ) {
        authNavigation(navController = navController)
        mainNavigation(navHostController = navController)
    }
}
