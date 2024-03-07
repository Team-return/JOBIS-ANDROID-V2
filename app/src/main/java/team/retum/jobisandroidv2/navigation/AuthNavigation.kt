package team.retum.jobisandroidv2.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import team.retum.jobis.change_password.navigation.confirmPassword
import team.retum.jobis.change_password.navigation.navigateToResetPassword
import team.retum.jobis.change_password.navigation.resetPassword
import team.retum.jobis.verify_email.navigation.navigateToVerifyEmail
import team.retum.jobis.verify_email.navigation.verifyEmail
import team.retum.jobisandroidv2.root.navigateToRoot
import team.retum.landing.navigation.NAVIGATION_LANDING
import team.retum.landing.navigation.landing
import team.retum.signin.navigation.navigateToSignIn
import team.retum.signin.navigation.signIn
import team.retum.signup.navigation.navigateToInputEmail
import team.retum.signup.navigation.navigateToSelectGender
import team.retum.signup.navigation.navigateToSetPassword
import team.retum.signup.navigation.navigateToSetProfile
import team.retum.signup.navigation.navigateToSignUp
import team.retum.signup.navigation.navigateToTerms
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
            onForgotPasswordClick = navController::navigateToVerifyEmail,
        )
        signUp(
            onBackPressed = navController::popBackStack,
            navigateToInputEmail = navController::navigateToInputEmail,
            navigateToSetPassword = navController::navigateToSetPassword,
            navigateToSelectGender = navController::navigateToSelectGender,
            navigateToSetProfile = navController::navigateToSetProfile,
            navigateToTerms = navController::navigateToTerms,
            navigateToRoot = navController::navigateToRoot,
        )
        verifyEmail(
            onBackPressed = navController::popBackStack,
            navigateToResetPassword = navController::navigateToResetPassword,
        )
        resetPassword(
            onBackPressed = navController::popBackStack,
            navigateToSignIn = navController::navigateToSignIn,
        )
        confirmPassword(
            onBackPressed = navController::popBackStack,
            navigateToResetPassword = navController::navigateToResetPassword,
        )
    }
}
