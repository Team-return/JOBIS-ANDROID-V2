package team.retum.jobis.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import team.retum.common.utils.ResourceKeys
import team.retum.jobis.recruitment.ui.SearchRecruitment

const val NAVIGATION_SEARCH_RECRUITMENT = "searchRecruitment"

fun NavGraphBuilder.searchRecruitment(
    onBackPressed: () -> Unit,
    onRecruitmentDetailsClick: (Long) -> Unit,
) {
    composable(
        route = "$NAVIGATION_SEARCH_RECRUITMENT/{${ResourceKeys.IS_WINTER_INTERN}}",
        arguments = listOf(
            navArgument(ResourceKeys.IS_WINTER_INTERN) { type = NavType.BoolType },
        ),
    ) {
        val isWinterIntern = it.arguments?.getBoolean(ResourceKeys.IS_WINTER_INTERN) ?: false
        SearchRecruitment(
            onBackPressed = onBackPressed,
            onRecruitmentDetailsClick = onRecruitmentDetailsClick,
            isWinterIntern = isWinterIntern,
        )
    }
}

fun NavController.navigateToSearchRecruitment(isWinterIntern: Boolean) {
    navigate("$NAVIGATION_SEARCH_RECRUITMENT/$isWinterIntern")
}
