package team.retum.jobisandroidv2.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import team.retum.jobisandroidv2.navigation.NAVIGATION_AUTH
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
        startDestination = NAVIGATION_AUTH,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
    ) {
        authNavigation(navController = navController)
        mainNavigation(navController = navController)
    }
}
