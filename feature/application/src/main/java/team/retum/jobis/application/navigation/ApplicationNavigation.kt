package team.retum.jobis.application.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import team.retum.jobis.application.model.CompanyInfo
import team.retum.jobis.application.ui.Application

const val NAVIGATION_APPLICATION = "application"
private val COMPANY_INFORMATION = "companyInformation"

fun NavGraphBuilder.application(
    onBackPressed: () -> Unit,
) {
    composable(
        route = "$NAVIGATION_APPLICATION/{$COMPANY_INFORMATION}",
        arguments = listOf(navArgument(COMPANY_INFORMATION) { NavType.StringType }),
    ) {
        val jsonString =
            it.arguments?.getString(COMPANY_INFORMATION) ?: throw NullPointerException()
        val companyInfo = Json.decodeFromString<CompanyInfo>(jsonString)
        Application(
            onBackPressed = onBackPressed,
            companyInfo = companyInfo,
        )
    }
}

fun NavController.navigateToApplication(
    companyProfileUrl: String,
    companyName: String,
) {
    val companyInfo = CompanyInfo(
        companyProfileUrl = companyProfileUrl,
        companyName = companyName,
    )
    val jsonString = Json.encodeToString(companyInfo)
    navigate("$NAVIGATION_APPLICATION/$jsonString")
}
