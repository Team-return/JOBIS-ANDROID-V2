package team.retum.employment.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import team.retum.employment.navigation.NAVIGATION_EMPLOYMENT
import team.retum.employment.viewmodel.EmploymentDetailViewModel
import team.retum.employment.viewmodel.EmploymentViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
internal fun EmploymentFilter(
    navController: NavHostController,
    onBackPressed: () -> Unit,
) {
    val parentEntry = remember(navController) {
        navController.getBackStackEntry(NAVIGATION_EMPLOYMENT)
    }
    val employmentDetailViewModel: EmploymentViewModel = hiltViewModel(parentEntry)
    EmploymentFilterScreen(
        onBackPressed = onBackPressed,
    )
}

@Composable
private fun EmploymentFilterScreen(
    onBackPressed: () -> Unit,
) {
    Column {
        JobisSmallTopAppBar(
            onBackPressed = onBackPressed,
        )
        JobisText(
            text = "필터 텍스트입니다",
            style = JobisTypography.Body,
        )
    }
}
