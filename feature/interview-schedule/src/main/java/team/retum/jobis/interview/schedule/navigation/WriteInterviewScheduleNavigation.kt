package team.retum.jobis.interview.schedule.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.jobis.interview.schedule.ui.WriteInterviewSchedule

const val NAVIGATION_WRITE_INTERVIEW_SCHEDULE = "writeInterviewSchedule"
const val ARG_INTERVIEW_ID = "interviewId"

fun NavGraphBuilder.writeInterviewSchedule(
    onBackPressed: () -> Unit,
) {
    composable(
        route = "$NAVIGATION_WRITE_INTERVIEW_SCHEDULE?$ARG_INTERVIEW_ID={$ARG_INTERVIEW_ID}",
        arguments = listOf(
            navArgument(ARG_INTERVIEW_ID) {
                type = NavType.LongType
                defaultValue = 0L
            }
        ),
    ) {
        WriteInterviewSchedule(
            onBackPressed = onBackPressed,
        )
    }
}

fun NavController.navigateToWriteInterviewSchedule() {
    navigate(NAVIGATION_WRITE_INTERVIEW_SCHEDULE)
}

fun NavController.navigateToWriteInterviewSchedule(interviewId: Long) {
    navigate("$NAVIGATION_WRITE_INTERVIEW_SCHEDULE?$ARG_INTERVIEW_ID=$interviewId")
}
