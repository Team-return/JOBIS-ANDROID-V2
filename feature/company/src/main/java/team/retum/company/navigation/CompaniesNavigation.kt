package team.retum.company.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import team.retum.company.ui.Companies

const val NAVIGATION_COMPANIES = "companies"

fun NavGraphBuilder.companies(
    onBackPressed: () -> Unit,
    onSearchClick: () -> Unit,
) {
    composable(NAVIGATION_COMPANIES) {
        Companies(
            onBackPressed = onBackPressed,
            onSearchClick = onSearchClick,
        )
    }
}

fun NavController.navigateToCompanies() {
    navigate(NAVIGATION_COMPANIES)
}
