package team.retum.bookmark.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.bookmark.ui.Bookmarks

const val NAVIGATION_BOOKMARK = "bookmark"

fun NavGraphBuilder.bookmarks(
    navController: NavController,
    onRecruitmentsClick: () -> Unit,
    onRecruitmentDetailClick: (Long) -> Unit,
) {
    composable(route = NAVIGATION_BOOKMARK) {
        Bookmarks(
            onBackPressed = { navController.popBackStack() },
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
