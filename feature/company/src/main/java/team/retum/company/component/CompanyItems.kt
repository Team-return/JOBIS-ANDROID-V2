package team.retum.company.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import team.retum.jobis.company.R
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.usecase.entity.CompaniesEntity
import team.retum.usecase.entity.HasRecruitment

private const val DEFAULT_SIZE_COMPANY_NAME = 1f
private const val DEFAULT_SIZE_HAS_RECRUITMENT = 0.6f
private const val DEFAULT_SIZE_TAKE = 0.5f

@Composable
internal fun CompanyItems(
    companies: List<CompaniesEntity.CompanyEntity>,
    onCompanyContentClick: (Long) -> Unit,
    whetherFetchNextPage: (lastVisibleItemIndex: Int) -> Boolean,
    fetchNextPage: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(JobisTheme.colors.background),
    ) {
        items(
            count = companies.size,
            key = { index -> index },
        ) { index ->
            CompanyItem(
                onCompanyContentClick = onCompanyContentClick,
                companyImageUrl = companies[index].logoUrl,
                companyName = companies[index].name,
                id = companies[index].id,
                take = companies[index].take,
                hasRecruitment = companies[index].hasRecruitment,
                takeText = companies[index].takeText,
            )
            if (whetherFetchNextPage(index)) {
                fetchNextPage()
            }
        }
    }
}

@Composable
private fun CompanyItem(
    modifier: Modifier = Modifier,
    onCompanyContentClick: (Long) -> Unit,
    companyImageUrl: String,
    companyName: String,
    id: Long,
    take: Float,
    hasRecruitment: HasRecruitment,
    takeText: String,
) {
    val hasRecruitmentText = when (hasRecruitment) {
        HasRecruitment.TRUE -> stringResource(id = R.string.has_recruitment)
        HasRecruitment.FALSE -> stringResource(id = R.string.has_not_recruitment)
        else -> ""
    }
    val hasRecruitmentColor = when (hasRecruitment) {
        HasRecruitment.TRUE -> JobisTheme.colors.primaryContainer
        else -> JobisTheme.colors.onSurface
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable(
                onClick = { onCompanyContentClick(id) },
                enabled = id != 0L,
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        AsyncImage(
            model = companyImageUrl,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    color = if (companyImageUrl.isEmpty()) {
                        JobisTheme.colors.surfaceVariant
                    } else {
                        Color.Unspecified
                    },
                ),
            contentDescription = "company image",
            contentScale = ContentScale.Crop,
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            JobisText(
                modifier = Modifier
                    .fillMaxWidth(DEFAULT_SIZE_COMPANY_NAME)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        color = if (companyName.isBlank()) {
                            JobisTheme.colors.surfaceVariant
                        } else {
                            Color.Unspecified
                        },
                    ),
                text = companyName,
                style = JobisTypography.SubHeadLine,
                color = JobisTheme.colors.inverseOnSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            JobisText(
                modifier = Modifier
                    .fillMaxWidth(DEFAULT_SIZE_HAS_RECRUITMENT)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        color = when (hasRecruitment) {
                            HasRecruitment.LOADING -> JobisTheme.colors.surfaceVariant
                            else -> Color.Unspecified
                        },
                    ),
                text = hasRecruitmentText,
                style = JobisTypography.SubBody,
                color = hasRecruitmentColor,
            )
            JobisText(
                modifier = Modifier
                    .fillMaxWidth(DEFAULT_SIZE_TAKE)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        color = when (takeText.isEmpty()) {
                            true -> JobisTheme.colors.surfaceVariant
                            else -> Color.Unspecified
                        },
                    ),
                text = takeText,
                style = JobisTypography.Description,
                color = JobisTheme.colors.inverseOnSurface,
            )
        }
    }
}
