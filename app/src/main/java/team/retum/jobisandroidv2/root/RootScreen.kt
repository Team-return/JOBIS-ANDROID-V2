package team.retum.jobisandroidv2.root

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import team.retum.bookmark.navigation.bookmarks
import team.retum.common.model.ApplicationData
import team.retum.home.R
import team.retum.home.navigation.NAVIGATION_HOME
import team.retum.home.navigation.home
import team.retum.jobis.navigation.myPage
import team.retum.jobis.recruitment.navigation.navigateToRecruitments
import team.retum.jobis.recruitment.navigation.recruitments
import team.retum.jobisandroidv2.ui.BottomNavigationBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun Root(
    applicationId: Long?,
    onAlarmClick: () -> Unit,
    onRecruitmentDetailsClick: (Long) -> Unit,
    onCompaniesClick: () -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: () -> Unit,
    onNotificationSettingClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onSelectInterestClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onReportBugClick: () -> Unit,
    onPostReviewClick: (Long) -> Unit,
    navigateToLanding: () -> Unit,
    navigateToApplication: (ApplicationData) -> Unit,
    navigateToRecruitmentDetails: (Long) -> Unit,
    navigatedFromNotifications: Boolean,
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    var applicationData by remember { mutableStateOf(ApplicationData.getDefaultApplicationData()) }

    RootScreen(
        sheetState = sheetState,
        applicationId = applicationId,
        onAlarmClick = onAlarmClick,
        showRejectionModal = {
            applicationData = it
            coroutineScope.launch {
                sheetState.show()
            }
        },
        onRecruitmentDetailsClick = onRecruitmentDetailsClick,
        onCompaniesClick = onCompaniesClick,
        onRecruitmentFilterClick = onRecruitmentFilterClick,
        onSearchRecruitmentClick = onSearchRecruitmentClick,
        onNotificationSettingClick = onNotificationSettingClick,
        onNoticeClick = onNoticeClick,
        onSelectInterestClick = onSelectInterestClick,
        onChangePasswordClick = onChangePasswordClick,
        onReportBugClick = onReportBugClick,
        rejectionReason = applicationData.rejectionReason,
        navigateToLanding = navigateToLanding,
        onPostReviewClick = onPostReviewClick,
        navigateToApplicationByRejectionBottomSheet = {
            coroutineScope.launch {
                sheetState.hide()
            }
            navigateToApplication(applicationData)
        },
        navigateToRecruitmentDetails = navigateToRecruitmentDetails,
        navigatedFromNotifications = navigatedFromNotifications,
        navigateToApplication = { navigateToApplication(it) },
    )
}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun RootScreen(
    navController: NavHostController = rememberNavController(),
    sheetState: ModalBottomSheetState,
    applicationId: Long?,
    onAlarmClick: () -> Unit,
    onRecruitmentDetailsClick: (Long) -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: () -> Unit,
    showRejectionModal: (ApplicationData) -> Unit,
    onCompaniesClick: () -> Unit,
    onNotificationSettingClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onSelectInterestClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onReportBugClick: () -> Unit,
    rejectionReason: String,
    navigateToLanding: () -> Unit,
    onPostReviewClick: (Long) -> Unit,
    navigateToApplicationByRejectionBottomSheet: () -> Unit,
    navigateToApplication: (ApplicationData) -> Unit,
    navigateToRecruitmentDetails: (Long) -> Unit,
    navigatedFromNotifications: Boolean,
) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            RejectionBottomSheet(
                reason = rejectionReason,
                onReApplyClick = navigateToApplicationByRejectionBottomSheet,
            )
        },
        sheetShape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp,
        ),
    ) {
        Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) {
            NavHost(
                navController = navController,
                startDestination = NAVIGATION_HOME,
                modifier = Modifier
                    .background(JobisTheme.colors.background)
                    .padding(bottom = it.calculateBottomPadding()),
            ) {
                home(
                    applicationId = applicationId,
                    onAlarmClick = onAlarmClick,
                    showRejectionModal = showRejectionModal,
                    onCompaniesClick = onCompaniesClick,
                    navigateToRecruitmentDetails = navigateToRecruitmentDetails,
                    navigatedFromNotifications = navigatedFromNotifications,
                    navigateToApplication = navigateToApplication,
                )
                recruitments(
                    onRecruitmentDetailsClick = onRecruitmentDetailsClick,
                    onRecruitmentFilterClick = onRecruitmentFilterClick,
                    onSearchRecruitmentClick = onSearchRecruitmentClick,
                )
                bookmarks(
                    onRecruitmentsClick = navController::navigateToRecruitments,
                    onRecruitmentDetailClick = onRecruitmentDetailsClick,
                )
                myPage(
                    onSelectInterestClick = onSelectInterestClick,
                    onChangePasswordClick = onChangePasswordClick,
                    onReportBugClick = onReportBugClick,
                    onNoticeClick = onNoticeClick,
                    onPostReviewClick = onPostReviewClick,
                    navigateToLanding = navigateToLanding,
                    onNotificationSettingClick = onNotificationSettingClick,
                )
            }
        }
    }
}

@Composable
private fun RejectionBottomSheet(
    reason: String,
    onReApplyClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(JobisTheme.colors.inverseSurface)
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 24.dp,
                bottom = 16.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        JobisText(
            text = stringResource(id = R.string.reason_rejection),
            style = JobisTypography.SubBody,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        JobisText(
            text = reason,
            style = JobisTypography.Body,
        )
    }
    JobisButton(
        modifier = Modifier.background(JobisTheme.colors.inverseSurface),
        text = stringResource(id = R.string.re_apply),
        onClick = onReApplyClick,
        color = ButtonColor.Primary,
    )
}
