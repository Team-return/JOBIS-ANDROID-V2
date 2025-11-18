package team.retum.employment.navigation

import androidx.activity.compose.BackHandler
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.employmentNavigation(
    navController: NavHostController,
    onEmploymentBackPressed: () -> Unit,
    onEmploymentClassClick: (Long) -> Unit,
    onEmploymentFilterClick: () -> Unit,
) {
    navigation(
        route = NAVIGATION_EMPLOYMENT,
        startDestination = EMPLOYMENT_LIST,
    ) {
        employment(
            navController = navController,
            onBackPressed = onEmploymentBackPressed,
            onClassClick = onEmploymentClassClick,
            onFilterClick = onEmploymentFilterClick,
        )
        employmentFilter(
            navController = navController,
            onBackPressed = onEmploymentBackPressed,
        )
        employmentDetail(
            navController = navController,
            onBackPressed = onEmploymentBackPressed,
        )
    }
}
