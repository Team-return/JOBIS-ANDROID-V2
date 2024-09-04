package team.retum.company.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.retum.company.component.CompanyItems
import team.retum.company.viewmodel.CompaniesSideEffect
import team.retum.company.viewmodel.CompaniesViewModel
import team.retum.jobis.company.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.usecase.entity.CompaniesEntity

@Composable
internal fun Companies(
    onBackPressed: () -> Unit,
    onSearchClick: () -> Unit,
    onCompanyContentClick: (Long) -> Unit,
    companiesViewModel: CompaniesViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        with(companiesViewModel) {
            fetchTotalCompanyPageCount()
            sideEffect.collect {
                when (it) {
                    is CompaniesSideEffect.FetchCompaniesError -> {
                        JobisToast.create(
                            context = context,
                            message = context.getString(R.string.toast_fetch_companies_error),
                            drawable = JobisIcon.Error,
                        ).show()
                    }
                }
            }
        }
    }

    CompaniesScreen(
        onBackPressed = onBackPressed,
        onSearchClick = onSearchClick,
        onCompanyContentClick = onCompanyContentClick,
        companies = companiesViewModel.companies.toPersistentList(),
        whetherFetchNextPage = companiesViewModel::whetherFetchNextPage,
        fetchNextPage = companiesViewModel::fetchCompanies,
    )
}

@Composable
private fun CompaniesScreen(
    onBackPressed: () -> Unit,
    onSearchClick: () -> Unit,
    onCompanyContentClick: (Long) -> Unit,
    companies: ImmutableList<CompaniesEntity.CompanyEntity>,
    whetherFetchNextPage: (lastVisibleItemIndex: Int) -> Boolean,
    fetchNextPage: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(
            title = stringResource(id = R.string.company),
            onBackPressed = onBackPressed,
        ) {
            JobisIconButton(
                drawableResId = JobisIcon.Search,
                contentDescription = "search",
                onClick = onSearchClick,
            )
        }
        CompanyItems(
            companies = companies,
            onCompanyContentClick = onCompanyContentClick,
            whetherFetchNextPage = whetherFetchNextPage,
            fetchNextPage = fetchNextPage,
        )
    }
}
