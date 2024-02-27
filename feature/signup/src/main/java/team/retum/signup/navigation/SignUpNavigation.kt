package team.retum.signup.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import team.retum.common.utils.ResourceKeys
import team.retum.signup.model.SignUpData
import team.retum.signup.model.toSignUpData

const val NAVIGATION_SIGN_UP = "signUp"

fun NavGraphBuilder.signUp(
    onBackPressed: () -> Unit,
    navigateToInputEmail: (SignUpData) -> Unit,
    navigateToSetPassword: (SignUpData) -> Unit,
    onSelectGenderClick: () -> Unit,
    onSetProfileClick: () -> Unit,
    onTermsClick: () -> Unit,
    onCompleteClick: () -> Unit,
) {
    navigation(
        route = NAVIGATION_SIGN_UP,
        startDestination = NAVIGATION_INPUT_PERSONAL_INFO,
    ) {
        inputPersonalInfo(
            onBackPressed = onBackPressed,
            navigateToInputEmail = navigateToInputEmail,
        )
        inputEmail(
            onBackPressed = onBackPressed,
            navigateToSetPassword = navigateToSetPassword,
        )
        setPassword(
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

internal fun NavBackStackEntry.getSignUpData(): SignUpData {
    val signUpData = arguments?.getString(ResourceKeys.SIGN_UP) ?: throw NullPointerException()
    return signUpData.toSignUpData()
}
