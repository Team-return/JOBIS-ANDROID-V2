package team.retum.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.common.model.ApplicationData
import team.retum.home.ui.Home

const val NAVIGATION_HOME = "home"

fun NavGraphBuilder.home(
    applicationId: Long?,
    onAlarmClick: () -> Unit,
    showRejectionModal: (ApplicationData) -> Unit,
    onCompaniesClick: () -> Unit,
    navigateToRecruitmentDetails: (Long) -> Unit,
    navigatedFromNotifications: Boolean,
    navigateToApplication: (ApplicationData) -> Unit,
) {
    composable(NAVIGATION_HOME) {
        Home(
            applicationId = applicationId,
            onAlarmClick = onAlarmClick,
            showRejectionModal = showRejectionModal,
            onCompaniesClick = onCompaniesClick,
            navigateToRecruitmentDetails = navigateToRecruitmentDetails,
            navigatedFromNotifications = navigatedFromNotifications,
            navigateToApplication = navigateToApplication,
        )
    }
}
