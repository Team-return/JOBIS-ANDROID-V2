package team.retum.jobisandroidv2.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.HorizontalDivider
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
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography

private val bottomMenus = listOf(
    BottomMenu.Home,
    BottomMenu.Recruitments,
    BottomMenu.Review,
    BottomMenu.MyPage,
)

@Composable
fun BottomNavigationBar(
    selectedRoute: String?,
    onHomeClick: () -> Unit,
    onRecruitmentsClick: () -> Unit,
    onReviewClick: () -> Unit,
    onMyPageClick: () -> Unit,
) {
    Column {
        HorizontalDivider(
            thickness = 0.3.dp,
            color = JobisTheme.colors.surfaceTint,
        )
        BottomAppBar(
            modifier = Modifier.fillMaxHeight(0.08f),
            contentColor = JobisTheme.colors.background,
            containerColor = JobisTheme.colors.background,
        ) {
            bottomMenus.forEach { menu ->
                val selected = selectedRoute?.startsWith(menu.route) == true
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
                        if (!selected) {
                            when (menu) {
                                BottomMenu.Home -> onHomeClick()
                                BottomMenu.Recruitments -> onRecruitmentsClick()
                                BottomMenu.Review -> onReviewClick()
                                BottomMenu.MyPage -> onMyPageClick()
                            }
                        }
                    },
                    icon = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = menu.icon),
                                contentDescription = menu.route,
                                tint = color,
                            )
                            Text(
                                text = stringResource(id = menu.title),
                                style = JobisTypography.Caption,
                                color = color,
                            )
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = JobisTheme.colors.background,
                    ),
                )
            }
        }
    }
}
