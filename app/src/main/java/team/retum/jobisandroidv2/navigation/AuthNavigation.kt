package team.retum.jobisandroidv2.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation

const val NAVIGATION_AUTH = "auth"

internal fun NavGraphBuilder.authNavigation() {
    navigation(
        route = NAVIGATION_AUTH,
        startDestination = "",
    ) {
        // TODO auth 관련 스크린 위치
    }
}
