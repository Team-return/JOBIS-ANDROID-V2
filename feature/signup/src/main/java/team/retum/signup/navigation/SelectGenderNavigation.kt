package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.signup.ui.SelectGender

const val NAVIGATION_SELECT_GENDER = "selectGender"

fun NavGraphBuilder.selectGender(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
) {
    composable(NAVIGATION_SELECT_GENDER) {
        SelectGender(
            onBackPressed = onBackPressed,
            onNextClick = onNextClick,
        )
    }
}

fun NavController.navigateToSelectGender() {
    navigate(NAVIGATION_SELECT_GENDER)
}
