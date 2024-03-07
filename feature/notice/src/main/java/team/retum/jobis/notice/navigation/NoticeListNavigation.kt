package team.retum.jobis.notice.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.notice.ui.NoticeList

const val NAVIGATION_NOTICE_LIST = "noticeList"

fun NavGraphBuilder.noticeList(
    onBackPressed: () -> Unit,
) {
    composable(NAVIGATION_NOTICE_LIST) {
        NoticeList(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToNoticeList() {
    navigate(NAVIGATION_NOTICE_LIST)
}
