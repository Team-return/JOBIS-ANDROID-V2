package team.retum.jobis.application.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import team.retum.common.model.ReApplyData
import team.retum.jobis.application.model.CompanyInfo
import team.retum.jobis.application.ui.Application

const val NAVIGATION_APPLICATION = "application"
private const val COMPANY_INFORMATION = "companyInformation"
private const val RECRUITMENT_ID = "recruitmentId"
private const val IS_RE_APPLY = "isReApply"
private const val APPLICATION_ID = "applicationId"

fun NavGraphBuilder.application(
    onBackPressed: () -> Unit,
) {
    composable(
        route = "$NAVIGATION_APPLICATION/{$RECRUITMENT_ID}/{$COMPANY_INFORMATION}/{$IS_RE_APPLY}/{$APPLICATION_ID}",
        arguments = listOf(
            navArgument(COMPANY_INFORMATION) { NavType.StringType },
            navArgument(RECRUITMENT_ID) { NavType.StringType },
            navArgument(IS_RE_APPLY) { type = NavType.BoolType },
            navArgument(APPLICATION_ID) { type = NavType.LongType },
        ),
    ) {
        val jsonString =
            it.arguments?.getString(COMPANY_INFORMATION) ?: throw NullPointerException()
        val companyInfo = Json.decodeFromString<CompanyInfo>(jsonString)
        val recruitmentId = it.arguments?.getString(RECRUITMENT_ID) ?: throw NullPointerException()
        val isReApply = it.arguments?.getBoolean(IS_RE_APPLY) ?: false
        val applicationId = it.arguments?.getLong(APPLICATION_ID) ?: 0
        Application(
            onBackPressed = onBackPressed,
            companyInfo = companyInfo,
            recruitmentId = recruitmentId,
            isReApply = isReApply,
            applicationId = applicationId,
        )
    }
}

fun NavController.navigateToApplication(
    reApplyData: ReApplyData,
    isReApply: Boolean = false,
) {
    val companyInfo = CompanyInfo(
        companyProfileUrl = reApplyData.companyLogoUrl.replace("/", " "),
        companyName = reApplyData.companyName,
    )
    val jsonString = Json.encodeToString(companyInfo)
    navigate("$NAVIGATION_APPLICATION/${reApplyData.recruitmentId}/$jsonString/$isReApply/${reApplyData.applicationId}")
}
