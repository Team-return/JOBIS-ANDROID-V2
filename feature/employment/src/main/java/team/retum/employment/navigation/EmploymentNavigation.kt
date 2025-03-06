package team.retum.employment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.employment.ui.Employment

const val NAVIGATION_EMPLOYMENT = "employment"

fun NavGraphBuilder.employment(
    onBackPressed: () -> Unit,
    onClassClick: () -> Unit,
) {
    composable(
        route = "$NAVIGATION_EMPLOYMENT/$ResourceKeys",
        arguments = listOf(
            navArgument(ResourceKeys.RATE) { NavType.FloatType },
            navArgument(ResourceKeys.TOTAL_STUDENT_COUNT) { NavType.LongType },
            navArgument(ResourceKeys.PASS_COUNT) { NavType.LongType },
        ),
    ) {
        val rate = it.arguments?.getFloat(ResourceKeys.RATE) ?: 0f
        val totalStudentCount = it.arguments?.getLong(ResourceKeys.RATE) ?: 0
        val passCount = it.arguments?.getLong(ResourceKeys.RATE) ?: 0
        Employment(
            onBackPressed = onBackPressed,
            onClassClick = onClassClick,
            rate = rate,
            totalStudentCount = totalStudentCount,
            passCount = passCount,
        )
    }
}

fun NavController.navigateToEmployment() {
    navigate(NAVIGATION_EMPLOYMENT)
}
