package team.retum.employment.navigation

import android.util.Log
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
        arguments = listOf(
            navArgument(ResourceKeys.CLASS_ID) { NavType.IntType }),
    ) {
        val classId = it.arguments?.getInt(ResourceKeys.CLASS_ID) ?: 0

        EmploymentDetail(
            onBackPressed = onBackPressed,
            classId = classId,
        )
    }
}

fun NavController.navigateToEmploymentDetail(classId: Int) {
    navigate("$EMPLOYMENT_DETAIL/$classId")
}
