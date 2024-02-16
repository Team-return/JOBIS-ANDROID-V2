package team.retum.company.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import coil.compose.AsyncImage
import team.retum.company.R
import team.retum.company.viewmodel.CompanyViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.textfield.JobisTextField
import team.retum.usecase.entity.CompaniesEntity

private const val IMAGE_URL = "https://jobis-store.s3.ap-northeast-2.amazonaws.com/"

@Composable
internal fun SearchCompanies(
    onBackPressed: () -> Unit,
    companyViewModel: CompanyViewModel = hiltViewModel(),
) {
    val state by companyViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.name) {
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
        name = state.name,
        onNameChange = { companyViewModel.setName(it) },
        companies = companyViewModel.companies.value.companies,
        checkCompanies = { companyViewModel.setCheckCompany(it) },
        pageCount = state.page,
        totalPageCount = state.totalPage,
    )
}

@Composable
private fun SearchCompaniesScreen(
    onBackPressed: () -> Unit,
    name: String?,
    onNameChange: (String) -> Unit,
    companies: List<CompaniesEntity.CompanyEntity>,
    checkCompanies: (Boolean) -> Unit,
    pageCount: Int,
    totalPageCount: Long,
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
            value = { name ?: "" },
            hint = "검색어를 입력해주세요",
            onValueChange = onNameChange,
        )
        if (companies.isNotEmpty() || name.isNullOrEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .background(JobisTheme.colors.background),
            ) {
                items(companies) {
                    SearchCompanyItem(
                        companyImageUrl = IMAGE_URL + it.logoUrl,
                        companyName = it.name,
                        id = it.id,
                        hasRecruitment = it.hasRecruitment,
                    )
                    if (it == companies.last() && pageCount.toLong() != totalPageCount) {
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
private fun SearchCompanyItem(
    modifier: Modifier = Modifier,
    companyImageUrl: String,
    companyName: String,
    id: Long,
    hasRecruitment: Boolean,
) {
    val hasRecruitmentText =
        stringResource(id = if (hasRecruitment) R.string.has_recruitment else R.string.has_not_recruitment)
    val hasRecruitmentColor =
        if (hasRecruitment) JobisTheme.colors.primaryContainer else JobisTheme.colors.onSurface
    Row(
        modifier = modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = companyImageUrl,
            modifier = Modifier.size(48.dp),
            contentDescription = "company image",
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            JobisText(
                text = companyName,
                style = JobisTypography.SubHeadLine,
                color = JobisTheme.colors.inverseOnSurface,
            )
            JobisText(
                text = hasRecruitmentText,
                style = JobisTypography.SubBody,
                color = hasRecruitmentColor,
            )
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