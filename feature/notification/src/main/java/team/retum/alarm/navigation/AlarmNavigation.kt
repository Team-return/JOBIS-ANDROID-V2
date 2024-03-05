package team.retum.alarm.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.alarm.ui.Alarm

const val NAVIGATION_ALARM = "alarm"

fun NavGraphBuilder.alarm(onBackPressed: () -> Unit) {
    composable(NAVIGATION_ALARM) {
        Alarm(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToAlarm() {
    navigate(NAVIGATION_ALARM)
}
