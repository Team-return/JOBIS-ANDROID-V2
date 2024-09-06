package team.retum.notification.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.notification.ui.NotificationSetting

const val NAVIGATION_NOTIFICATION_SETTING = "notificationSetting"

fun NavGraphBuilder.notificationSetting(
    onBackPressed: () -> Unit,
) {
    composable(NAVIGATION_NOTIFICATION_SETTING) {
        NotificationSetting(
            onBackPressed = onBackPressed,
        )
    }
}

fun NavController.navigateToNotificationSetting() {
    navigate(NAVIGATION_NOTIFICATION_SETTING)
}
