package team.retum.company.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.retum.company.component.CompanyItems
import team.retum.company.viewmodel.CompaniesState
import team.retum.company.viewmodel.CompaniesViewModel
import team.retum.jobis.company.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.empty.EmptyContent
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.usecase.entity.CompaniesEntity

@Composable
internal fun SearchCompanies(
    onBackPressed: () -> Unit,
    onCompanyContentClick: (Long) -> Unit,
    searchCompaniesViewModel: CompaniesViewModel = hiltViewModel(),
) {
    val state by searchCompaniesViewModel.state.collectAsStateWithLifecycle()

    SearchCompaniesScreen(
        onBackPressed = onBackPressed,
        onNameChange = searchCompaniesViewModel::setName,
        companies = searchCompaniesViewModel.companies.toPersistentList(),
        onCompanyContentClick = onCompanyContentClick,
        state = state,
        whetherFetchNextPage = searchCompaniesViewModel::whetherFetchNextPage,
        fetchNextPage = searchCompaniesViewModel::fetchCompanies,
    )
}

@Composable
private fun SearchCompaniesScreen(
    onBackPressed: () -> Unit,
    onNameChange: (String) -> Unit,
    companies: ImmutableList<CompaniesEntity.CompanyEntity>,
    onCompanyContentClick: (Long) -> Unit,
    state: CompaniesState,
    whetherFetchNextPage: (lastVisibleItemIndex: Int) -> Boolean,
    fetchNextPage: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(onBackPressed = onBackPressed)
        JobisTextField(
            value = { state.name ?: "" },
            hint = stringResource(id = R.string.search_hint),
            onValueChange = onNameChange,
        )
        CompanyItems(
            companies = companies,
            onCompanyContentClick = onCompanyContentClick,
            whetherFetchNextPage = whetherFetchNextPage,
            fetchNextPage = fetchNextPage,
        )
        if (state.showCompaniesEmptyContent) {
            EmptyContent(
                title = stringResource(id = R.string.not_found_company),
                description = stringResource(id = R.string.double_check),
            )
        }
    }
}
