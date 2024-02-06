package team.retum.jobisandroidv2.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import team.retum.jobisandroidv2.root.navigateToRoot
import team.retum.landing.navigation.NAVIGATION_LANDING
import team.retum.landing.navigation.landing
import team.retum.signin.navigation.navigateToSignIn
import team.retum.signin.navigation.signIn
import team.retum.signup.navigation.navigateToInputEmail
import team.retum.signup.navigation.navigateToSelectGender
import team.retum.signup.navigation.navigateToSetProfile
import team.retum.signup.navigation.navigateToSettingPassword
import team.retum.signup.navigation.navigateToSignUp
import team.retum.signup.navigation.signUp

const val NAVIGATION_AUTH = "auth"

internal fun NavGraphBuilder.authNavigation(navController: NavController) {
    navigation(
        route = NAVIGATION_AUTH,
        startDestination = NAVIGATION_LANDING,
    ) {
        landing(
            onSignInClick = navController::navigateToSignIn,
            onSignUpClick = navController::navigateToSignUp,
        )
        signIn(
            onBackPressed = navController::popBackStack,
            onSignInSuccess = navController::navigateToRoot,
        )
        signUp(
            onBackPressed = navController::popBackStack,
            onInputEmailClick = navController::navigateToInputEmail,
            onInputPasswordClick = navController::navigateToSettingPassword,
            onSelectGenderClick = navController::navigateToSelectGender,
            onSetProfileClick = navController::navigateToSetProfile,
        )
    }
}
