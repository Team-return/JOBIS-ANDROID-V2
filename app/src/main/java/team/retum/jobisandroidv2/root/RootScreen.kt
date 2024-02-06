package team.retum.jobisandroidv2.root

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import team.retum.alarm.navigation.navigateToAlarm
import team.retum.home.navigation.NAVIGATION_HOME
import team.retum.home.navigation.home
import team.retum.jobisandroidv2.ui.BottomNavigationBar

@Composable
fun Root(navHostController: NavHostController) {
    val navController = rememberNavController()

    RootScreen(
        navController = navController,
        navHostController = navHostController,
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun RootScreen(
    navController: NavHostController,
    navHostController: NavController,
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) },
    ) {
        NavHost(
            navController = navController,
            startDestination = NAVIGATION_HOME,
        ) {
            home(onAlarmClick = navHostController::navigateToAlarm)
        }
    }
}
