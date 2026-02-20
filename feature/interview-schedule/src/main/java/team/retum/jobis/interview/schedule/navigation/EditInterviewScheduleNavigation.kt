package team.retum.jobis.interview.schedule.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.jobis.interview.schedule.ui.EditInterviewSchedule

const val NAVIGATION_EDIT_INTERVIEW_SCHEDULE = "editInterviewSchedule"
const val INTERVIEW_ID = "interviewId"

fun NavGraphBuilder.editInterviewSchedule(
    onBackPressed: () -> Unit,
) {
    composable(
        route = "$NAVIGATION_EDIT_INTERVIEW_SCHEDULE?$INTERVIEW_ID={$INTERVIEW_ID}",
        arguments = listOf(
            navArgument(INTERVIEW_ID) {
                type = NavType.LongType
                defaultValue = 0L
            },
        ),
    ) {
        EditInterviewSchedule(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToEditInterviewSchedule(interviewId: Long) {
    navigate("$NAVIGATION_EDIT_INTERVIEW_SCHEDULE?$INTERVIEW_ID=$interviewId")
}
