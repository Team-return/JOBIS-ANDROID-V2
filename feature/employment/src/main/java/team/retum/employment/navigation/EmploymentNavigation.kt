package team.retum.employment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import team.retum.employment.ui.Employment

const val NAVIGATION_EMPLOYMENT = "employment"
const val EMPLOYMENT_LIST = "employment/list"

fun NavGraphBuilder.employment(
    navController: NavHostController,
    onBackPressed: () -> Unit,
    onClassClick: (Long) -> Unit,
    onFilterClick: () -> Unit,
) {
    composable(
        route = EMPLOYMENT_LIST,
    ) {
        Employment(
            navController = navController,
            onBackPressed = onBackPressed,
            onClassClick = onClassClick,
            onFilterClick = onFilterClick,
        )
    }
}

fun NavController.navigateToEmployment() {
    navigate(EMPLOYMENT_LIST)
}
