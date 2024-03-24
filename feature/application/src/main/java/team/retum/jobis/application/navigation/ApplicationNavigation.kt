package team.retum.jobis.application.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import team.retum.common.model.ApplicationData
import team.retum.jobis.application.ui.Application

const val NAVIGATION_APPLICATION = "application"
private const val APPLICATION_DATA = "companyInformation"

fun NavGraphBuilder.application(
    onBackPressed: () -> Unit,
) {
    composable(
        route = "$NAVIGATION_APPLICATION/{$APPLICATION_DATA}",
        arguments = listOf(navArgument(APPLICATION_DATA) { type = NavType.StringType }),
    ) {
        val jsonString =
            it.arguments?.getString(APPLICATION_DATA) ?: ApplicationData.getDefaultApplicationData()
                .toString()
        val applicationData = Json.decodeFromString<ApplicationData>(jsonString)
        Application(
            onBackPressed = onBackPressed,
            applicationData = applicationData,
        )
    }
}

fun NavController.navigateToApplication(applicationData: ApplicationData) {
    val jsonString = Json.encodeToString(applicationData)
    navigate("$NAVIGATION_APPLICATION/$jsonString")
}
