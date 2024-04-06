package team.retum.company.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import team.retum.company.component.CompanyItem
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
        companies = companiesViewModel.companies,
        whetherFetchNextPage = companiesViewModel::whetherFetchNextPage,
        fetchNextPage = companiesViewModel::fetchCompanies,
    )
}

@Composable
private fun CompaniesScreen(
    onBackPressed: () -> Unit,
    onSearchClick: () -> Unit,
    onCompanyContentClick: (Long) -> Unit,
    companies: List<CompaniesEntity.CompanyEntity>,
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
                painter = painterResource(JobisIcon.Search),
                contentDescription = "search",
                onClick = onSearchClick,
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(JobisTheme.colors.background),
        ) {
            itemsIndexed(companies) { index, item ->
                CompanyItem(
                    onCompanyContentClick = onCompanyContentClick,
                    companyImageUrl = item.logoUrl,
                    companyName = item.name,
                    id = item.id,
                    take = item.take,
                    hasRecruitment = item.hasRecruitment,
                )
                if (whetherFetchNextPage(index)) {
                    fetchNextPage()
                }
            }
        }
    }
}
