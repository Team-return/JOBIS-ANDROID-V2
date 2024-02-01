package team.retum.jobisandroidv2.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import team.retum.jobisandroidv2.navigation.NAVIGATION_HOME

const val NAVIGATION_ROOT = "root"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RootScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) },
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = NAVIGATION_HOME,
        ) {
            // TODO 내비게이션 바 해당 메뉴 스크린 위치
        }
    }
}
