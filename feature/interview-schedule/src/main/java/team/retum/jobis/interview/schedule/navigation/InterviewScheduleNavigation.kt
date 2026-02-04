package team.retum.jobis.interview.schedule.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.jobis.interview.schedule.ui.InterviewSchedule

const val NAVIGATION_INTERVIEW_SCHEDULE = "interviewSchedule"

fun NavGraphBuilder.interviewSchedule(
    onBackPressed: () -> Unit,
) {
    composable(NAVIGATION_INTERVIEW_SCHEDULE) {
        InterviewSchedule(
            onBackPressed = onBackPressed,
        )
    }
}

fun NavController.navigateToInterviewSchedule() {
    navigate(NAVIGATION_INTERVIEW_SCHEDULE)
}