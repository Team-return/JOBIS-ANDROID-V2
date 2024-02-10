package team.retum.recruitment

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val NAVIGATION_RECRUITMENTS = "recruitments"

fun NavGraphBuilder.recruitments() {
    composable(NAVIGATION_RECRUITMENTS) {
        Recruitments()
    }
}
