package team.retum.bug

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val NAVIGATION_REPORT_BUG = "reportBug"

fun NavGraphBuilder.reportBug() {
    composable(NAVIGATION_REPORT_BUG) {
        ReportBug()
    }
}

fun NavController.navigateToReportBug() {
    navigate(NAVIGATION_REPORT_BUG)
}
