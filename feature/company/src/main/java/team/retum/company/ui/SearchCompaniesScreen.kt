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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.company.component.CompanyItem
import team.retum.company.viewmodel.CompaniesState
import team.retum.company.viewmodel.CompaniesViewModel
import team.retum.jobis.company.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
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
        companies = searchCompaniesViewModel.companies,
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
    companies: List<CompaniesEntity.CompanyEntity>,
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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(JobisTheme.colors.background),
        ) {
            if (companies.isNotEmpty()) {
                itemsIndexed(companies) { index, item ->
                    CompanyItem(
                        onCompanyContentClick = onCompanyContentClick,
                        companyImageUrl = item.logoUrl,
                        companyName = item.name,
                        id = item.id,
                        hasRecruitment = item.hasRecruitment,
                        take = item.take,
                    )
                    if (whetherFetchNextPage(index)) {
                        fetchNextPage()
                    }
                }
            }
        }
        if (state.showCompaniesEmptyContent) {
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
