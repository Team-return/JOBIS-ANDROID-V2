package team.retum.jobisandroidv2.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography

private val bottomMenus = listOf(
    BottomMenu.Home,
    BottomMenu.Recruitments,
    BottomMenu.Bookmark,
    BottomMenu.MyPage,
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val selectedRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    Column {
        Divider(thickness = 0.3.dp)
        BottomAppBar(
            modifier = Modifier.fillMaxHeight(0.08f),
            contentColor = JobisTheme.colors.background,
            containerColor = JobisTheme.colors.background,
        ) {
            bottomMenus.forEach {
                val selected = selectedRoute == it.route
                val color by animateColorAsState(
                    targetValue = if (selected) {
                        JobisTheme.colors.onBackground
                    } else {
                        JobisTheme.colors.surfaceTint
                    },
                    label = "",
                )

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(it.route) {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = it.icon),
                                contentDescription = it.route,
                                tint = color,
                            )
                            Text(
                                text = stringResource(id = it.title),
                                style = JobisTypography.Caption,
                                color = color,
                            )
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = JobisTheme.colors.background),
                )
            }
        }
    }
}
