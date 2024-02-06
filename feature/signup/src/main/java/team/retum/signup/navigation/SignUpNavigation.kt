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
    onSetProfileClick: () -> Unit,
    onTermsClick: () -> Unit,
    onCompleteClick: () -> Unit,
) {
    navigation(
        route = NAVIGATION_SIGN_UP,
        startDestination = NAVIGATION_INPUT_PERSONAL_INFO,
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
        selectGender(
            onBackPressed = onBackPressed,
            onNextClick = onSetProfileClick,
        )
        setProfile(
            onBackPressed = onBackPressed,
            onNextClick = onTermsClick,
        )
        terms(
            onBackPressed = onBackPressed,
            onCompleteClick = onCompleteClick,
        )
    }
}

fun NavController.navigateToSignUp() {
    navigate(NAVIGATION_SIGN_UP)
}
