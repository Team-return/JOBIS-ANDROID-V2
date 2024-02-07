package team.retum.bookmark.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.bookmark.ui.Bookmark

const val NAVIGATION_BOOKMARK = "bookmark"

fun NavGraphBuilder.bookmark() {
    composable(NAVIGATION_BOOKMARK) {
        Bookmark()
    }
}

fun NavController.navigateToBookmark() {
    navigate(NAVIGATION_BOOKMARK)
}
