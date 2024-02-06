package team.retum.alarm.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.alarm.ui.AlarmScreen

const val NAVIGATION_ALARM = "alarm"

fun NavGraphBuilder.alarm(onBackPressed: () -> Unit) {
    composable(route = NAVIGATION_ALARM) {
        AlarmScreen(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToAlarm() {
    navigate(NAVIGATION_ALARM)
}
