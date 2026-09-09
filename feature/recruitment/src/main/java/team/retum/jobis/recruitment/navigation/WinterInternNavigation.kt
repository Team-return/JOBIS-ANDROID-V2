package team.retum.jobis.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.jobis.recruitment.ui.WinterIntern

private const val NAVIGATION_WINTER_INTERN = "winterIntern"
private const val IS_WINTER_INTERN_AVAILABLE = "isWinterInternAvailable"

fun NavGraphBuilder.winterIntern(
    onBackPressed: () -> Unit,
    onRecruitmentDetailsClick: (Long) -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: (Boolean) -> Unit,
) {
    composable(
        route = "$NAVIGATION_WINTER_INTERN/{$IS_WINTER_INTERN_AVAILABLE}",
        arguments = listOf(
            navArgument(IS_WINTER_INTERN_AVAILABLE) {
                type = NavType.BoolType
            },
        ),
    ) {
        WinterIntern(
            isWinterInternAvailable = it.arguments?.getBoolean(IS_WINTER_INTERN_AVAILABLE) ?: false,
            onBackPressed = onBackPressed,
            onRecruitmentDetailsClick = onRecruitmentDetailsClick,
            onRecruitmentFilterClick = onRecruitmentFilterClick,
            onSearchRecruitmentClick = onSearchRecruitmentClick,
        )
    }
}

fun NavController.navigateToWinterIntern(isWinterInternAvailable: Boolean) {
    navigate("$NAVIGATION_WINTER_INTERN/$isWinterInternAvailable")
}
