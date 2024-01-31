package team.retum.jobisandroidv2.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import team.retum.jobisandroidv2.R
import team.retum.jobisandroidv2.navigation.NAVIGATION_HOME

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

    data object Recruitment : BottomMenu(
        route = "recruitment",
        icon = R.drawable.ic_recruitment,
        title = R.string.recruitment,
    )

    data object Bookmark : BottomMenu(
        route = "bookmark",
        icon = R.drawable.ic_bookmark,
        title = R.string.bookmark,
    )

    data object MyPage : BottomMenu(
        route = "myPage",
        icon = R.drawable.ic_my_page,
        title = R.string.my_page,
    )
}
