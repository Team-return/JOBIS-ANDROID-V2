package team.retum.company.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import team.retum.company.viewmodel.CompanyDetailsState
import team.retum.company.viewmodel.CompanyDetailsViewModel
import team.retum.jobis.company.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.usecase.entity.company.CompanyDetailsEntity

@Composable
internal fun CompanyDetails(
    companyId: Long,
    onBackPressed: () -> Unit,
    companyDetailsViewModel: CompanyDetailsViewModel = hiltViewModel(),
) {
    val state by companyDetailsViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        with(companyDetailsViewModel) {
            setCompanyId(companyId = companyId)
            fetchCompanyDetails()
        }
    }

    CompanyDetailsScreen(
        companyId = companyId,
        onBackPressed = onBackPressed,
        state = state,
    )
}

@Composable
private fun CompanyDetailsScreen(
    companyId: Long,
    onBackPressed: () -> Unit,
    state: CompanyDetailsState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        JobisSmallTopAppBar(
            title = stringResource(id = R.string.company_details),
            onBackPressed = onBackPressed,
        )
        CompanyMainInformation(
            companyLogoUrl = state.companyDetailsEntity.companyProfileUrl,
            name = state.companyDetailsEntity.companyName,
            description = state.companyDetailsEntity.companyIntroduce,
        )
        CompanyInformations(companyDetailsEntity = state.companyDetailsEntity)
    }
}

@Composable
private fun CompanyMainInformation(
    companyLogoUrl: String,
    name: String,
    description: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 12.dp,
                horizontal = 24.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = JobisTheme.colors.inverseSurface,
                        shape = RoundedCornerShape(8.dp),
                    ),
                model = companyLogoUrl,
                contentDescription = "company logo",
                contentScale = ContentScale.Crop,
            )
            JobisText(
                text = name,
                style = JobisTypography.HeadLine,
            )
        }
        JobisText(
            text = description,
            style = JobisTypography.Body,
        )
    }
}

@Composable
private fun CompanyInformations(companyDetailsEntity: CompanyDetailsEntity) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        with(companyDetailsEntity) {
            CompanyInformation(
                title = stringResource(id = R.string.representative_name),
                detail = representativeName,
            )
            CompanyInformation(
                title = stringResource(id = R.string.founded_at),
                detail = foundedAt,
            )
            CompanyInformation(
                title = stringResource(id = R.string.worker_number),
                detail = workerNumber,
            )
            CompanyInformation(
                title = stringResource(id = R.string.take),
                detail = take,
            )
            CompanyInformation(
                title = stringResource(id = R.string.main_address),
                detail = mainAddress,
            )
            CompanyInformation(
                title = stringResource(id = R.string.sub_address),
                detail = subAddress,
            )
            CompanyInformation(
                title = stringResource(id = R.string.manager),
                detail = managerName,
            )
            CompanyInformation(
                title = stringResource(id = R.string.manager_phone_number),
                detail = managerPhoneNo,
            )
            CompanyInformation(
                title = stringResource(id = R.string.sub_manager),
                detail = subManagerName,
            )
            CompanyInformation(
                title = stringResource(id = R.string.sub_manager_phone_number),
                detail = subManagerPhoneNo,
            )
            CompanyInformation(
                title = stringResource(id = R.string.email),
                detail = email,
            )
            CompanyInformation(
                title = stringResource(id = R.string.fax),
                detail = fax,
            )
        }
    }
}

@Composable
private fun CompanyInformation(
    title: String,
    detail: String?,
) {
    if (!detail.isNullOrBlank()) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 24.dp,
                    vertical = 12.dp,
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            JobisText(
                text = title,
                style = JobisTypography.Description,
                color = JobisTheme.colors.onSurfaceVariant,
            )
            JobisText(
                text = detail,
                style = JobisTypography.SubBody,
                color = JobisTheme.colors.onSurface,
            )
        }
    }
}
