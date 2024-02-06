package team.retum.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.signup.ui.SetProfile

const val NAVIGATION_SET_PROFILE = "setProfile"

fun NavGraphBuilder.setProfile(
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit,
){
    composable(NAVIGATION_SET_PROFILE){
        SetProfile(
            onBackPressed = onBackPressed,
            onNextClick = onNextClick,
        )
    }
}

fun NavController.navigateToSetProfile(){
    navigate(NAVIGATION_SET_PROFILE)
}
