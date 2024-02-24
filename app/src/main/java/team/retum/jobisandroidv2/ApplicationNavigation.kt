package team.retum.jobisandroidv2

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobisandroidv2.ui.Application

const val NAVIGATION_APPLICATION = "application"

fun NavGraphBuilder.application(
    onBackPressed: () -> Unit,
) {
    composable(NAVIGATION_APPLICATION) {
        Application(
            onBackPressed = onBackPressed,
        )
    }
}

fun NavController.navigateToApplication() {
    navigate(NAVIGATION_APPLICATION)
}
