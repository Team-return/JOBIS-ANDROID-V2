package team.retum.employment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import team.retum.employment.ui.EmploymentFilter

const val EMPLOYMENT_FILTER = "employment/filter"

fun NavGraphBuilder.employmentFilter(
    navController: NavHostController,
    onBackPressed: () -> Unit,
) {
    composable(
        route = EMPLOYMENT_FILTER,
    ) {
        EmploymentFilter(
            navController = navController,
            onBackPressed = onBackPressed,
        )
    }
}

fun NavController.navigateToEmploymentFilter() {
    navigate(EMPLOYMENT_FILTER)
}
