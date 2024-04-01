package team.retum.jobisandroidv2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import team.retum.jobisandroidv2.navigation.NAVIGATION_AUTH
import team.retum.jobisandroidv2.navigation.authNavigation
import team.retum.jobisandroidv2.navigation.mainNavigation
import team.retum.jobisdesignsystemv2.foundation.JobisTheme

@Composable
internal fun JobisApp() {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier
            .background(JobisTheme.colors.background)
            .navigationBarsPadding()
            .statusBarsPadding(),
        navController = navController,
        startDestination = NAVIGATION_AUTH,
    ) {
        authNavigation(navController = navController)
        mainNavigation(navController = navController)
    }
}
