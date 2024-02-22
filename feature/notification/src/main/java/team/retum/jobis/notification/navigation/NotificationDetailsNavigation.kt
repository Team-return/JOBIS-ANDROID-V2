package team.retum.jobis.notification.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.notification.ui.NotificationDetails

const val NAVIGATION_NOTIFICATION_DETAILS = "notificationDetails/"
const val NOTIFICATION_ID = "{notification-id}"

fun NavGraphBuilder.notificationDetails(
    onBackPressed: () -> Unit,
) {
    composable(NAVIGATION_NOTIFICATION_DETAILS + NOTIFICATION_ID) {
        NotificationDetails(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToNotificationDetails(notificationId: Long) {
    navigate(NAVIGATION_NOTIFICATION_DETAILS + notificationId)
}
