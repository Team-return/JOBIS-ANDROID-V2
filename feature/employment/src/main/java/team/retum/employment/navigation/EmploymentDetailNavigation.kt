package team.retum.employment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.employment.ui.EmploymentDetail

const val EMPLOYMENT_DETAIL = "employmentDetail"

fun NavGraphBuilder.employmentDetail(
    onBackPressed: () -> Unit,
) {
    composable(
        route = "$EMPLOYMENT_DETAIL/{${ResourceKeys.CLASS_ID}}",
        arguments = listOf(navArgument(ResourceKeys.CLASS_ID) { type = NavType.StringType }),
    ) {
        val classId = it.arguments?.getString(ResourceKeys.CLASS_ID) ?: throw NullPointerException()
        EmploymentDetail(
            onBackPressed = onBackPressed,
            classId = classId,
        )
    }
}

fun NavController.navigateToEmploymentDetail(classId: String) {
    navigate("$EMPLOYMENT_DETAIL/$classId")
}
