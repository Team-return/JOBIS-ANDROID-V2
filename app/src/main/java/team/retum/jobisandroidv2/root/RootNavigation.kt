package team.retum.jobisandroidv2.root

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val NAVIGATION_ROOT = "root"

fun NavGraphBuilder.root(
    onAlarmClick: () -> Unit,
    onRecruitmentDetailsClick: (Long) -> Unit,
) {
    composable(NAVIGATION_ROOT) {
        Root(
            onAlarmClick = onAlarmClick,
            onRecruitmentDetailsClick = onRecruitmentDetailsClick,
        )
    }
}

fun NavController.navigateToRoot() {
    navigate(NAVIGATION_ROOT)
}
