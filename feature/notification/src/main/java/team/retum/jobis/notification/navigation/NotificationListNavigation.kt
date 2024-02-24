package team.retum.jobis.notification.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.notification.ui.NotificationList

const val NAVIGATION_NOTIFICATION_LIST = "notificationList"

fun NavGraphBuilder.notificationList(
    onBackPressed: () -> Unit,
) {
    composable(NAVIGATION_NOTIFICATION_LIST) {
        NotificationList(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToNotificationList() {
    navigate(NAVIGATION_NOTIFICATION_LIST)
}
