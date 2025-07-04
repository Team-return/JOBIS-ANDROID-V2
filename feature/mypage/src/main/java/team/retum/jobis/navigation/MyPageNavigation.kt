package team.retum.jobis.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.ui.MyPage

const val NAVIGATION_MY_PAGE = "myPage"

fun NavGraphBuilder.myPage(
    onSelectInterestClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onReportBugClick: () -> Unit,
    onNoticeClick: () -> Unit,
    navigateToLanding: () -> Unit,
    onPostReviewClick: (Long) -> Unit,
    onNotificationSettingClick: () -> Unit,
) {
    composable(NAVIGATION_MY_PAGE) {
        MyPage(
            onSelectInterestClick = onSelectInterestClick,
            onChangePasswordClick = onChangePasswordClick,
            onReportBugClick = onReportBugClick,
            onNoticeClick = onNoticeClick,
            navigateToLanding = navigateToLanding,
            onPostReviewClick = onPostReviewClick,
            onNotificationSettingClick = onNotificationSettingClick,
        )
    }
}

fun NavController.navigateToMyPage() {
    navigate(NAVIGATION_MY_PAGE) {
        popUpTo(0) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
