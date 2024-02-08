package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.signup.ui.SetPassword

const val NAVIGATION_SET_PASSWORD = "setPassword"

fun NavGraphBuilder.setPassword(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
) {
    composable(NAVIGATION_SET_PASSWORD) {
        SetPassword(
            onBackPressed = onBackPressed,
            onNextClick = onNextClick,
        )
    }
}

fun NavController.navigateToSetPassword() {
    navigate(NAVIGATION_SET_PASSWORD)
}
