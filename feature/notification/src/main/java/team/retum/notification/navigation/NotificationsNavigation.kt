package team.retum.notification.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.notification.ui.Notifications

const val NAVIGATION_NOTIFICATIONS = "notifications"

fun NavGraphBuilder.notifications(
    onBackPressed: () -> Unit,
    navigateToRecruitment: (Long) -> Unit,
    navigateToHome: (Long?) -> Unit,
) {
    composable(NAVIGATION_NOTIFICATIONS) {
        Notifications(
            onBackPressed = onBackPressed,
            navigateToRecruitment = navigateToRecruitment,
            navigateToHome = navigateToHome,
        )
    }
}

fun NavController.navigateToNotification() {
    navigate(NAVIGATION_NOTIFICATIONS)
}
