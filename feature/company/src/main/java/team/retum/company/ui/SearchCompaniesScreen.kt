package team.retum.company.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import team.retum.company.component.CompanyItem
import team.retum.company.viewmodel.CompanyState
import team.retum.company.viewmodel.CompanyViewModel
import team.retum.jobis.company.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.usecase.entity.CompaniesEntity

const val SEARCH_DELAY: Long = 200

@Composable
internal fun SearchCompanies(
    onBackPressed: () -> Unit,
    onCompanyContentClick: (Long) -> Unit,
    companyViewModel: CompanyViewModel = hiltViewModel(),
) {
    val state by companyViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.name) {
        delay(SEARCH_DELAY)
        if (state.checkCompany && state.name?.isNotBlank() ?: "".isNotBlank()) {
            companyViewModel.fetchTotalCompanyCount(name = state.name)
            companyViewModel.fetchCompanies(
                page = state.page,
                name = state.name,
            )
        }
    }
    SearchCompaniesScreen(
        onBackPressed = onBackPressed,
        onNameChange = companyViewModel::setName,
        companies = companyViewModel.companies.value.companies,
        checkCompanies = companyViewModel::setCheckCompany,
        onCompanyContentClick = onCompanyContentClick,
        state = state,
    )
}

@Composable
private fun SearchCompaniesScreen(
    onBackPressed: () -> Unit,
    onNameChange: (String) -> Unit,
    companies: List<CompaniesEntity.CompanyEntity>,
    checkCompanies: (Boolean) -> Unit,
    onCompanyContentClick: (Long) -> Unit,
    state: CompanyState,
) {
    checkCompanies(false)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(onBackPressed = onBackPressed)
        JobisTextField(
            title = "",
            value = { state.name ?: "" },
            hint = stringResource(id = R.string.search_hint),
            onValueChange = onNameChange,
        )
        if (companies.isNotEmpty() || state.name.isNullOrEmpty()) {
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
                        hasRecruitment = it.hasRecruitment,
                        take = it.take,
                    )
                    if (it == companies.last() && state.page.toLong() != state.totalPage) {
                        checkCompanies(true)
                    }
                }
            }
        } else {
            checkCompanies(true)
            EmptySearchContent()
        }
    }
}

@Composable
private fun EmptySearchContent() {
    Column(
        modifier = Modifier
            .background(JobisTheme.colors.background)
            .fillMaxSize()
            .padding(vertical = 160.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(128.dp),
            painter = painterResource(id = R.drawable.ic_empty_company),
            contentDescription = "empty bookmark",
        )
        Spacer(modifier = Modifier.height(16.dp))
        JobisText(
            text = stringResource(id = R.string.not_found_company),
            style = JobisTypography.HeadLine,
        )
        Spacer(modifier = Modifier.height(8.dp))
        JobisText(
            text = stringResource(id = R.string.double_check),
            style = JobisTypography.Body,
            color = JobisTheme.colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
