package team.retum.employment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.employment.ui.Employment

const val NAVIGATION_EMPLOYMENT = "employment"

fun NavGraphBuilder.employment(

) {
    composable(
        route = NAVIGATION_EMPLOYMENT,
        arguments = listOf(

        )
    ) {
        Employment()
    }
}

fun NavController.navigateToEmployment() {
    navigate(NAVIGATION_EMPLOYMENT)
}
