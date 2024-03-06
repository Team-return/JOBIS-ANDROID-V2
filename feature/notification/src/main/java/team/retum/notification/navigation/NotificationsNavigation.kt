package team.retum.alarm.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.alarm.ui.Notification

const val NAVIGATION_NOTIFICATION = "notification"

fun NavGraphBuilder.notification(
    onBackPressed: () -> Unit,
    onNotificationDetailsClick: (Long) -> Unit,
) {
    composable(NAVIGATION_NOTIFICATION) {
        Notification(
            onBackPressed = onBackPressed,
            onNotificationDetailsClick = onNotificationDetailsClick,
        )
    }
}

fun NavController.navigateToNotification() {
    navigate(NAVIGATION_NOTIFICATION)
}
