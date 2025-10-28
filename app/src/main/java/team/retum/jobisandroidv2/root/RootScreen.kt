package team.retum.jobisandroidv2.root

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import team.retum.review.navigation.review

/**
 * Hosts the app's root UI, wires navigation and action callbacks, and controls the rejection modal state
 * forwarded to RootScreen.
 *
 * @param applicationId Optional application ID to preselect when entering the app.
 * @param navigateToLanding Navigates to the landing (auth/entry) screen.
 * @param navigateToApplication Navigates to the application flow with the provided ApplicationData.
 * @param navigateToRecruitmentDetails Navigates to the recruitment details screen for the given recruitment ID.
 * @param navigatedFromNotifications `true` when the current navigation was initiated from a notification; forwarded to Home.
 */
@Composable
internal fun Root(
    applicationId: Long?,
    onAlarmClick: () -> Unit,
    onEmploymentClick: () -> Unit,
    onWinterInternClick: () -> Unit,
    onRecruitmentDetailsClick: (Long) -> Unit,
    onCompaniesClick: () -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: (Boolean) -> Unit,
    onNotificationSettingClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onSelectInterestClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onReportBugClick: () -> Unit,
    onPostReviewClick: (String, Long) -> Unit,
    onReviewFilterClick: () -> Unit,
    onSearchReviewClick: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
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
        onEmploymentClick = onEmploymentClick,
        onWinterInternClick = onWinterInternClick,
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
        onReviewFilterClick = onReviewFilterClick,
        onSearchReviewClick = onSearchReviewClick,
        onReviewDetailClick = onReviewDetailClick,
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

/**
 * Composes the app's main navigation scaffold with a bottom navigation bar and a modal rejection bottom sheet.
 *
 * Hosts the navigation graph (Home, Recruitments, Bookmarks, Review, MyPage), wires screen-level callbacks into each destination,
 * and displays a RejectionBottomSheet using the provided sheet state and rejection reason.
 *
 * @param sheetState Controls visibility and state of the modal bottom sheet shown for rejection details.
 * @param applicationId If non-null, the Home destination will receive this application id to display related content.
 * @param showRejectionModal Callback invoked with ApplicationData to open the rejection bottom sheet populated for that application.
 * @param rejectionReason Text shown inside the rejection bottom sheet.
 * @param navigateToApplicationByRejectionBottomSheet Callback invoked when the user chooses to re-apply from the rejection bottom sheet.
 * @param navigateToApplication Callback used to navigate to an application detail screen with the provided ApplicationData.
 * @param navigateToRecruitmentDetails Callback used to navigate to a recruitment details screen with the provided recruitment id.
 * @param navigatedFromNotifications Indicates whether navigation to Home originated from a notifications entry point (affects Home destination behavior).
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun RootScreen(
    navController: NavHostController = rememberNavController(),
    sheetState: ModalBottomSheetState,
    applicationId: Long?,
    onAlarmClick: () -> Unit,
    onEmploymentClick: () -> Unit,
    onWinterInternClick: () -> Unit,
    onRecruitmentDetailsClick: (Long) -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: (Boolean) -> Unit,
    showRejectionModal: (ApplicationData) -> Unit,
    onCompaniesClick: () -> Unit,
    onNotificationSettingClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onSelectInterestClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onReportBugClick: () -> Unit,
    rejectionReason: String,
    navigateToLanding: () -> Unit,
    onPostReviewClick: (String, Long) -> Unit,
    onReviewFilterClick: () -> Unit,
    onSearchReviewClick: () -> Unit,
    onReviewDetailClick: (Long) -> Unit,
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
                    onEmploymentClick = onEmploymentClick,
                    onWinterInternClick = onWinterInternClick,
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
                review(
                    onReviewFilterClick = onReviewFilterClick,
                    onSearchReviewClick = onSearchReviewClick,
                    onReviewDetailClick = onReviewDetailClick,
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