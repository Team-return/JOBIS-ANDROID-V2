package team.retum.jobis.notice.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.jobis.notice.ui.NoticeDetails

const val NAVIGATION_NOTICE_DETAILS = "noticeDetails/"
const val NOTICE_ID = "noticeId"

fun NavGraphBuilder.noticeDetails(
    onBackPressed: () -> Unit,
) {
    composable(
        route = "$NAVIGATION_NOTICE_DETAILS{$NOTICE_ID}",
        arguments = listOf(navArgument(NOTICE_ID) { NavType.StringType }),
    ) {
        NoticeDetails(
            onBackPressed = onBackPressed,
            noticeId = it.arguments?.getString(NOTICE_ID)?.toLong() ?: throw NullPointerException(),
        )
    }
}

fun NavController.navigateToNoticeDetails(noticeId: Long) {
    navigate(NAVIGATION_NOTICE_DETAILS + noticeId)
}
