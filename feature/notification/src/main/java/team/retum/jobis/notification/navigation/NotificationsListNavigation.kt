package team.retum.jobis.notification.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.notification.ui.NotificationsList

const val NAVIGATION_NOTIFICATIONS_LIST = "notificationsList"

fun NavGraphBuilder.notificationList(
    onBackPressed: () -> Unit,
) {
    composable(NAVIGATION_NOTIFICATIONS_LIST) {
        NotificationsList(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToNotificationsList() {
    navigate(NAVIGATION_NOTIFICATIONS_LIST)
}
