package team.retum.jobis.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.splash.ui.Splash

const val NAVIGATION_SPLASH = "splash"

fun NavGraphBuilder.splash(
    navigateToSignIn: () -> Unit,
    navigateToRoot: () -> Unit,
) {
    composable(NAVIGATION_SPLASH) {
        Splash(
            navigateToSignIn = navigateToSignIn,
            navigateToRoot = navigateToRoot,
        )
    }
}
