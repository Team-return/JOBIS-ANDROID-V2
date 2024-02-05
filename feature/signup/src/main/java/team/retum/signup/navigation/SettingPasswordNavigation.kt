package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.signup.ui.SettingPasswordScreen

const val NAVIGATION_SIGN_UP_SETTING_PASSWORD = "signUp/settingPassword"

fun NavGraphBuilder.settingPassword(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    composable(route = NAVIGATION_SIGN_UP_SETTING_PASSWORD) {
        SettingPasswordScreen(
            onBackClick = onBackClick,
            onNextClick = onNextClick,
        )
    }
}

fun NavController.navigateToSettingPassword() {
    navigate(NAVIGATION_SIGN_UP_SETTING_PASSWORD)
}
