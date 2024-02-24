package team.returm.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.returm.mypage.ui.MyPage

const val NAVIGATION_MYPAGE = "mypage"

fun NavGraphBuilder.mypage(onNoticeClick: () -> Unit) {
    composable(NAVIGATION_MYPAGE) {
        MyPage(onNoticeClick = onNoticeClick)
    }
}

fun NavController.navigateToMyPage() {
    navigate(NAVIGATION_MYPAGE)
}
