package team.retum.jobisandroidv2.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import team.retum.home.navigation.NAVIGATION_HOME
import team.retum.jobis.recruitment.navigation.NAVIGATION_RECRUITMENTS
import team.retum.jobisandroidv2.R
import team.returm.mypage.navigation.NAVIGATION_MY_PAGE

sealed class BottomMenu(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
) {
    data object Home : BottomMenu(
        route = NAVIGATION_HOME,
        icon = R.drawable.ic_home,
        title = R.string.home,
    )

    data object Recruitments : BottomMenu(
        route = NAVIGATION_RECRUITMENTS,
        icon = R.drawable.ic_recruitment,
        title = R.string.recruitment,
    )

    data object Bookmark : BottomMenu(
        route = "bookmark",
        icon = R.drawable.ic_bookmark,
        title = R.string.bookmark,
    )

    data object MyPage : BottomMenu(
        route = NAVIGATION_MY_PAGE,
        icon = R.drawable.ic_my_page,
        title = R.string.my_page,
    )
}
