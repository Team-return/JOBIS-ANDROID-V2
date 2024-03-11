package team.retum.bug.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.bug.ui.ReportBug

const val NAVIGATION_REPORT_BUG = "reportBug"

fun NavGraphBuilder.reportBug(onBackPressed: () -> Unit) {
    composable(NAVIGATION_REPORT_BUG) {
        ReportBug(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToReportBug() {
    navigate(NAVIGATION_REPORT_BUG)
}
