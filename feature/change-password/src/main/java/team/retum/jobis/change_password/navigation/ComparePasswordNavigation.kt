package team.retum.jobis.change_password.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.change_password.ui.ComparePassword

const val NAVIGATION_COMPARE_PASSWORD = "confirmPassword"

fun NavGraphBuilder.confirmPassword(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
) {
    composable(NAVIGATION_COMPARE_PASSWORD) {
        ComparePassword(
            onBackPressed = onBackPressed,
            onNextClick = onNextClick,
        )
    }
}

fun NavController.navigateToComparePassword() {
    navigate(NAVIGATION_COMPARE_PASSWORD)
}
