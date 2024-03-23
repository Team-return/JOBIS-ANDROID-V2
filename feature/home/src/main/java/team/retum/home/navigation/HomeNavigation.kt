package team.retum.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.common.model.ReApplyData
import team.retum.home.ui.Home

const val NAVIGATION_HOME = "home"

fun NavGraphBuilder.home(
    applicationId: Long?,
    onAlarmClick: () -> Unit,
    showRejectionModal: (ReApplyData) -> Unit,
    onCompaniesClick: () -> Unit,
    navigateToRecruitmentDetails: (Long) -> Unit,
    navigatedFromNotifications: Boolean,
) {
    composable(NAVIGATION_HOME) {
        Home(
            applicationId = applicationId,
            onAlarmClick = onAlarmClick,
            showRejectionModal = showRejectionModal,
            onCompaniesClick = onCompaniesClick,
            navigateToRecruitmentDetails = navigateToRecruitmentDetails,
            navigatedFromNotifications = navigatedFromNotifications,
        )
    }
}
