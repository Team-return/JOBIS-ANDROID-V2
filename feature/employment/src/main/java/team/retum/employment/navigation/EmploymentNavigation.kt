package team.retum.employment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.employment.ui.Employment

const val NAVIGATION_EMPLOYMENT = "employment"

fun NavGraphBuilder.employment(
    onBackPressed: () -> Unit,
    onClassClick: (Long) -> Unit,
) {
    composable(
        route = NAVIGATION_EMPLOYMENT,
    ) {
        Employment(
            onBackPressed = onBackPressed,
            onClassClick = onClassClick,
        )
    }
}

fun NavController.navigateToEmployment() {
    navigate(NAVIGATION_EMPLOYMENT)
}
