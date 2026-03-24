package team.retum.bookmark.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.bookmark.ui.Bookmarks

const val NAVIGATION_BOOKMARK = "bookmark"

fun NavGraphBuilder.bookmarks(
    onRecruitmentsClick: () -> Unit,
    onRecruitmentDetailClick: (Long) -> Unit,
    onBackPressed: () -> Unit,
) {
    composable(route = NAVIGATION_BOOKMARK) {
        Bookmarks(
            onBackPressed = onBackPressed,
            onRecruitmentsClick = onRecruitmentsClick,
            onRecruitmentDetailClick = onRecruitmentDetailClick,
        )
    }
}

fun NavController.navigateToBookmark() {
    navigate(NAVIGATION_BOOKMARK) {
        launchSingleTop = true
    }
}
