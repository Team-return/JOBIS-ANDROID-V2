package team.retum.jobisandroidv2.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation

const val NAVIGATION_HOME = "home"

internal fun NavGraphBuilder.homeNavigation() {
    navigation(
        route = NAVIGATION_HOME,
        startDestination = "",
    ) {
        // TODO home 관련 스크린 위치
    }
}
