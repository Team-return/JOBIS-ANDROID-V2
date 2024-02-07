package team.retum.jobisandroidv2.root

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import team.retum.alarm.navigation.navigateToAlarm
import team.retum.bookmark.navigation.bookmark
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
        bottomBar = {
            Column {
                Divider(thickness = 0.3.dp)
                BottomNavigationBar(navController = navController)
            }
        },
    ) {
        NavHost(
            navController = navController,
            startDestination = NAVIGATION_HOME,
            modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
        ) {
            home(onAlarmClick = navHostController::navigateToAlarm)
            bookmark()
        }
    }
}
