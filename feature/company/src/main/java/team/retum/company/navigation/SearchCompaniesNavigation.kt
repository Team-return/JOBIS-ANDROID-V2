package team.retum.company.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.company.ui.SearchCompanies

const val NAVIGATION_SEARCH_COMPANIES = "searchCompanies"

fun NavGraphBuilder.searchCompanies(onBackPressed: () -> Unit) {
    composable(NAVIGATION_SEARCH_COMPANIES) {
        SearchCompanies(onBackPressed = onBackPressed)
    }
}

fun NavController.navigateToSearchCompanies() {
    navigate(NAVIGATION_SEARCH_COMPANIES)
}
