package team.retum.jobisandroidv2.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import team.retum.bookmark.navigation.NAVIGATION_BOOKMARK
import team.retum.home.navigation.NAVIGATION_HOME
import team.retum.jobis.R
import team.retum.jobis.navigation.NAVIGATION_MY_PAGE
import team.retum.jobis.recruitment.navigation.NAVIGATION_RECRUITMENTS
import team.retum.review.navigation.review_read.NAVIGATION_REVIEW

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

    data object Review : BottomMenu(
        route = NAVIGATION_REVIEW,
        icon = R.drawable.ic_review,
        title = R.string.review,
    )

    data object Bookmark : BottomMenu(
        route = NAVIGATION_BOOKMARK,
        icon = R.drawable.ic_bookmark,
        title = R.string.bookmark,
    )

    data object MyPage : BottomMenu(
        route = NAVIGATION_MY_PAGE,
        icon = R.drawable.ic_my_page,
        title = R.string.my_page,
    )
}
