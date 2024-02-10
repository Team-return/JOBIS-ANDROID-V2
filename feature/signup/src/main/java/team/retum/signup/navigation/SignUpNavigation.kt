package team.retum.signup.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import team.retum.common.util.Keys
import java.io.Serializable

const val NAVIGATION_SIGN_UP = "signUp"

fun NavGraphBuilder.signUp(
    onBackPressed: () -> Unit,
    navigateToInputEmail: (String, Long) -> Unit,
    onSetPasswordClick: () -> Unit,
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
            onNextClick = onSetPasswordClick,
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

@Suppress("Deprecation")
internal fun NavBackStackEntry.getSignUpData(): Serializable {
    return arguments?.getSerializable(Keys.SIGN_UP) ?: throw NullPointerException()
}
