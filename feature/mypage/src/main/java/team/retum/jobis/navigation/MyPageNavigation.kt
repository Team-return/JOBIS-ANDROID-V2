package team.retum.jobis.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.ui.MyPage

const val NAVIGATION_MY_PAGE = "myPage"

/**
 * Registers the MyPage destination on this NavGraphBuilder.
 *
 * @param onSelectInterestClick Invoked when the user chooses to select interests.
 * @param onChangePasswordClick Invoked when the user requests to change their password.
 * @param onReportBugClick Invoked when the user reports a bug.
 * @param onNoticeClick Invoked when the user opens notices.
 * @param navigateToLanding Invoked to navigate back to the landing screen.
 * @param onPostReviewClick Invoked to post a review; receives a String identifier and a Long value.
 * @param onNotificationSettingClick Invoked when the user opens notification settings.
 */
fun NavGraphBuilder.myPage(
    onSelectInterestClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onReportBugClick: () -> Unit,
    onNoticeClick: () -> Unit,
    navigateToLanding: () -> Unit,
    onPostReviewClick: (String, Long) -> Unit,
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
    navigate(NAVIGATION_MY_PAGE)
}