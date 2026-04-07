package team.retum.jobis.interview.schedule.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.interview.schedule.ui.AddInterviewSchedule

const val NAVIGATION_ADD_INTERVIEW_SCHEDULE = "addInterviewSchedule"

fun NavGraphBuilder.addInterviewSchedule(
    onBackPressed: () -> Unit,
) {
    composable(route = NAVIGATION_ADD_INTERVIEW_SCHEDULE) {
        AddInterviewSchedule(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToAddInterviewSchedule() {
    navigate(NAVIGATION_ADD_INTERVIEW_SCHEDULE)
}
