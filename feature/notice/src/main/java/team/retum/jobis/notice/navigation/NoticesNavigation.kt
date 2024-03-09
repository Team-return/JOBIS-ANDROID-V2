package team.retum.jobis.notice.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.notice.ui.Notices

const val NAVIGATION_NOTICES = "notices"

fun NavGraphBuilder.notices(
    onBackPressed: () -> Unit,
) {
    composable(NAVIGATION_NOTICES) {
        Notices(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToNotices() {
    navigate(NAVIGATION_NOTICES)
}
