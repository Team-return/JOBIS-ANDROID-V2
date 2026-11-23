package team.retum.jobisandroidv2.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import team.retum.jobisandroidv2.JobisNavigator
import team.retum.jobisandroidv2.navigation.NAVIGATION_AUTH
import team.retum.jobisandroidv2.navigation.authNavigation
import team.retum.jobisandroidv2.navigation.mainNavigation
import team.retum.jobisandroidv2.notification.PushNotificationData
import team.retum.jobisandroidv2.rememberJobisNavigator
import team.retum.jobisandroidv2.root.NAVIGATION_ROOT
import team.retum.jobisdesignsystemv2.foundation.JobisTheme

@Composable
internal fun JobisApp(
    pushNotificationData: PushNotificationData?,
    onPushNotificationHandled: () -> Unit,
) {
    val navigator: JobisNavigator = rememberJobisNavigator()
    val navBackStackEntry by navigator.navController.currentBackStackEntryAsState()

    LaunchedEffect(pushNotificationData, navBackStackEntry?.destination?.route) {
        if (
            pushNotificationData != null &&
            navBackStackEntry?.destination?.route?.startsWith(NAVIGATION_ROOT) == true
        ) {
            navigator.navigateToPushNotification(pushNotificationData)
            onPushNotificationHandled()
        }
    }

    NavHost(
        modifier = Modifier
            .background(JobisTheme.colors.background)
            .navigationBarsPadding()
            .statusBarsPadding(),
        navController = navigator.navController,
        startDestination = NAVIGATION_AUTH,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        authNavigation(navigator = navigator)
        mainNavigation(navigator = navigator)
    }
}
