package team.retum.company.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.company.component.CompanyItem
import team.retum.company.viewmodel.CompanySideEffect
import team.retum.company.viewmodel.CompanyViewModel
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
    companyViewModel: CompanyViewModel = hiltViewModel(),
) {
    val state by companyViewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        companyViewModel.fetchTotalCompanyCount(name = state.name)
        companyViewModel.sideEffect.collect {
            when (it) {
                CompanySideEffect.ServerDown -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.error),
                    ).show()
                }
            }
        }
    }

    LaunchedEffect(state.checkCompany) {
        if (state.checkCompany) {
            companyViewModel.fetchCompanies(
                page = state.page,
                name = state.name,
            )
        }
    }

    CompaniesScreen(
        onBackPressed = onBackPressed,
        onSearchClick = onSearchClick,
        onCompanyContentClick = onCompanyContentClick,
        companies = companyViewModel.companies.value.companies,
        checkCompanies = companyViewModel::setCheckCompany,
        pageCount = state.page,
        totalPageCount = state.totalPage,
    )
}

@Composable
private fun CompaniesScreen(
    onBackPressed: () -> Unit,
    onSearchClick: () -> Unit,
    onCompanyContentClick: (Long) -> Unit,
    companies: List<CompaniesEntity.CompanyEntity>,
    checkCompanies: (Boolean) -> Unit,
    pageCount: Int,
    totalPageCount: Long,
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
        if (companies.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .background(JobisTheme.colors.background),
            ) {
                items(companies) {
                    CompanyItem(
                        onCompanyContentClick = onCompanyContentClick,
                        companyImageUrl = it.logoUrl,
                        companyName = it.name,
                        id = it.id,
                        take = it.take,
                        hasRecruitment = it.hasRecruitment,
                    )
                    if (it == companies.last() && pageCount.toLong() != totalPageCount) {
                        checkCompanies(true)
                    }
                }
            }
        } else {
            checkCompanies(true)
        }
        checkCompanies(false)
    }
}
