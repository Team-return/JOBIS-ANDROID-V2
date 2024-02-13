package team.retum.jobisandroidv2.root

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import team.retum.bookmark.navigation.bookmark
import team.retum.home.navigation.NAVIGATION_HOME
import team.retum.home.navigation.home
import team.retum.jobisandroidv2.ui.BottomNavigationBar
import team.retum.recruitment.navigation.recruitments

@Composable
internal fun Root(
    onAlarmClick: () -> Unit,
    onRecruitmentDetailClick: () -> Unit,
) {
    val navController = rememberNavController()

    RootScreen(
        navController = navController,
        onAlarmClick = onAlarmClick,
        onRecruitmentDetailClick = onRecruitmentDetailClick,
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun RootScreen(
    navController: NavHostController,
    onAlarmClick: () -> Unit,
    onRecruitmentDetailClick: () -> Unit,
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) },
    ) {
        NavHost(
            navController = navController,
            startDestination = NAVIGATION_HOME,
            modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
        ) {
            home(onAlarmClick = onAlarmClick)
            recruitments(onRecruitmentDetailClick = onRecruitmentDetailClick)
            bookmark()
        }
    }
}
