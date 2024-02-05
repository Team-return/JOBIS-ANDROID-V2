package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation

const val NAVIGATION_SIGN_UP = "signUp"

fun NavGraphBuilder.signUp(
    onBackPressed: () -> Unit,
    onInputEmailClick: () -> Unit,
    onInputPasswordClick: () -> Unit,
    onSelectGenderClick: () -> Unit,
) {
    navigation(
        route = NAVIGATION_SIGN_UP,
        startDestination = NAVIGATION_SIGN_UP_INPUT_PERSONAL_INFO,
    ) {
        inputPersonalInformation(
            onBackPressed = onBackPressed,
            onNextClick = onInputEmailClick,
        )
        inputEmail(
            onBackPressed = onBackPressed,
            onNextClick = onInputPasswordClick,
        )
        settingPassword(
            onBackPressed = onBackPressed,
            onNextClick = onSelectGenderClick,
        )
        // TODO 다음 스크린 작업 시 onNextClick 추가
        selectGender(
            onBackPressed = onBackPressed,
            onNextClick = {},
        )
    }
}

fun NavController.navigateToSignUp() {
    navigate(NAVIGATION_SIGN_UP)
}
