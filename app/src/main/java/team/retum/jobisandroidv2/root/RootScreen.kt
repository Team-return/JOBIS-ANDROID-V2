package team.retum.jobisandroidv2.root

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import team.retum.bookmark.navigation.bookmarks
import team.retum.home.R
import team.retum.home.navigation.NAVIGATION_HOME
import team.retum.home.navigation.home
import team.retum.jobis.recruitment.navigation.navigateToRecruitments
import team.retum.jobis.recruitment.navigation.recruitments
import team.retum.jobisandroidv2.ui.BottomNavigationBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.returm.mypage.navigation.mypage

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun Root(
    onAlarmClick: () -> Unit,
    onRecruitmentDetailsClick: (Long) -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: () -> Unit,
) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )

    RootScreen(
        navController = navController,
        sheetState = sheetState,
        onAlarmClick = onAlarmClick,
        onRejectionReasonClick = {
            coroutineScope.launch {
                sheetState.show()
            }
        },
        onRecruitmentDetailsClick = onRecruitmentDetailsClick,
        onRecruitmentFilterClick = onRecruitmentFilterClick,
        onSearchRecruitmentClick = onSearchRecruitmentClick,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun RootScreen(
    navController: NavHostController,
    sheetState: ModalBottomSheetState,
    onAlarmClick: () -> Unit,
    onRecruitmentDetailsClick: (Long) -> Unit,
    onRecruitmentFilterClick: () -> Unit,
    onSearchRecruitmentClick: () -> Unit,
    onRejectionReasonClick: () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            RejectionBottomSheet(
                reason = "reason",
                onReApplyClick = {},
            )
        },
        sheetShape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp,
        ),
    ) {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController = navController) },
        ) {
            NavHost(
                navController = navController,
                startDestination = NAVIGATION_HOME,
                modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
            ) {
                home(
                    onAlarmClick = onAlarmClick,
                    onRejectionReasonClick = onRejectionReasonClick,
                )
                recruitments(
                    onRecruitmentDetailsClick = onRecruitmentDetailsClick,
                    onRecruitmentFilterClick = onRecruitmentFilterClick,
                    onSearchRecruitmentClick = onSearchRecruitmentClick,
                )
                bookmarks(onRecruitmentsClick = navController::navigateToRecruitments)
                mypage()
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
        modifier = Modifier.padding(
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
        text = stringResource(id = R.string.re_apply),
        onClick = onReApplyClick,
        color = ButtonColor.Primary,
    )
}
