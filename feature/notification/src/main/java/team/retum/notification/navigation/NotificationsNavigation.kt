package team.retum.notification.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.notification.ui.Notification

const val NAVIGATION_NOTIFICATION = "notification"

fun NavGraphBuilder.notification(
    onBackPressed: () -> Unit,
    navigateToDetail: (Long) -> Unit,
) {
    composable(NAVIGATION_NOTIFICATION) {
        Notification(
            onBackPressed = onBackPressed,
            navigateToDetail = navigateToDetail,
        )
    }
}

fun NavController.navigateToNotification() {
    navigate(NAVIGATION_NOTIFICATION)
}
