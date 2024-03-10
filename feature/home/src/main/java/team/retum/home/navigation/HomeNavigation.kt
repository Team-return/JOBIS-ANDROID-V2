package team.retum.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.common.model.ReApplyData
import team.retum.home.ui.Home

const val NAVIGATION_HOME = "home"

fun NavGraphBuilder.home(
    onAlarmClick: () -> Unit,
    showRejectionModal: (ReApplyData) -> Unit,
    onCompaniesClick: () -> Unit,
) {
    composable(NAVIGATION_HOME) {
        Home(
            onAlarmClick = onAlarmClick,
            showRejectionModal = showRejectionModal,
            onCompaniesClick = onCompaniesClick,
        )
    }
}
