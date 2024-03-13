package team.retum.jobis.interests.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.interests.ui.Interests

const val NAVIGATION_INTERESTS = "interests"
fun NavGraphBuilder.interests(onBackPressed: () -> Unit) {
    composable(NAVIGATION_INTERESTS) {
        Interests(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToInterests() {
    navigate(NAVIGATION_INTERESTS)
}
