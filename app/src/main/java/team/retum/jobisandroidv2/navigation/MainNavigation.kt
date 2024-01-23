package team.retum.jobisandroidv2.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation

const val NAVIGATION_MAIN = "main"

fun NavGraphBuilder.mainNavigation() {
    navigation(
        route = NAVIGATION_MAIN,
        // TODO startDestination 추후 변경
        startDestination = "a",
    ) {
        // TODO main 관련 스크린 위치
    }
}
