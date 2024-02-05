package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.signup.ui.SettingPasswordScreen

const val NAVIGATION_SETTING_PASSWORD = "settingPassword"

fun NavGraphBuilder.settingPassword(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
) {
    composable(NAVIGATION_SETTING_PASSWORD) {
        SettingPasswordScreen(
            onBackPressed = onBackPressed,
            onNextClick = onNextClick,
        )
    }
}

fun NavController.navigateToSettingPassword() {
    navigate(NAVIGATION_SETTING_PASSWORD)
}
