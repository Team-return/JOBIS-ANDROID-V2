package team.retum.jobis.notification.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.notification.ui.NoticeDetails

const val NAVIGATION_NOTICE_DETAILS = "noticeDetails/"
const val NOTICE_ID = "{notice-id}"

fun NavGraphBuilder.notificationDetails(
    onBackPressed: () -> Unit,
) {
    composable(NAVIGATION_NOTICE_DETAILS + NOTICE_ID) {
        NoticeDetails(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToNoticeDetails(noticeId: Long) {
    navigate(NAVIGATION_NOTICE_LIST + noticeId)
}
