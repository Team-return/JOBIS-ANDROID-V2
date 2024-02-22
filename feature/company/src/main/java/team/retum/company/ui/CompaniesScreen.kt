package team.retum.company.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import team.retum.company.viewmodel.CompanyViewModel
import team.retum.jobis.company.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.usecase.entity.CompaniesEntity

private const val IMAGE_URL = "https://jobis-store.s3.ap-northeast-2.amazonaws.com/"

@Composable
internal fun Companies(
    onBackPressed: () -> Unit,
    onSearchClick: () -> Unit,
    companyViewModel: CompanyViewModel = hiltViewModel(),
) {
    val state by companyViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        companyViewModel.fetchTotalCompanyCount(name = state.name)
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
                        companyImageUrl = IMAGE_URL + it.logoUrl,
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
        } else checkCompanies(true)
        checkCompanies(false)
    }
}

@Composable
private fun CompanyItem(
    modifier: Modifier = Modifier,
    companyImageUrl: String,
    companyName: String,
    id: Long,
    take: Float,
    hasRecruitment: Boolean,
) {
    val hasRecruitmentText =
        stringResource(id = if (hasRecruitment) R.string.has_recruitment else R.string.has_not_recruitment)
    val hasRecruitmentColor =
        if (hasRecruitment) JobisTheme.colors.primaryContainer else JobisTheme.colors.onSurface
    Row(
        modifier = modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
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
            JobisText(
                text = "연매출 ${take}억",
                style = JobisTypography.Description,
                color = JobisTheme.colors.inverseOnSurface,
            )
        }
    }
}
