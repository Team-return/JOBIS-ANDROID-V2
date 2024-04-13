package team.retum.jobis.recruitment.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import team.retum.common.enums.MilitarySupport
import team.retum.jobis.recruitment.R
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.usecase.entity.RecruitmentsEntity

private const val DEFAULT_SIZE_WHETHER_MILITARY_SUPPORTED = 0.6f

@Composable
internal fun RecruitmentItems(
    recruitments: List<RecruitmentsEntity.RecruitmentEntity>,
    onRecruitmentClick: (Long) -> Unit,
    onBookmarkClick: (Long) -> Unit,
    whetherFetchNextPage: (Int) -> Boolean,
    fetchNextPage: () -> Unit,
) {
    LazyColumn {
        itemsIndexed(items = recruitments) { index, recruitment ->
            var bookmarked = recruitment.bookmarked
            RecruitmentItem(
                recruitment = recruitment,
                onClick = onRecruitmentClick,
                bookmarkIcon = if (bookmarked) {
                    JobisIcon.BookmarkOn
                } else {
                    JobisIcon.BookmarkOff
                },
                onBookmarked = {
                    onBookmarkClick(it)
                    bookmarked = !bookmarked
                },
            )
            if (whetherFetchNextPage(index)) {
                fetchNextPage()
            }
        }
    }
}

@Composable
private fun RecruitmentItem(
    recruitment: RecruitmentsEntity.RecruitmentEntity,
    onClick: (recruitId: Long) -> Unit,
    @DrawableRes bookmarkIcon: Int,
    onBookmarked: (recruitId: Long) -> Unit,
) {
    val whetherMilitarySupported = when (recruitment.militarySupport) {
        MilitarySupport.TRUE -> stringResource(id = R.string.military_supported)
        MilitarySupport.FALSE -> stringResource(id = R.string.military_not_supported)
        else -> ""
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 12.dp,
                horizontal = 24.dp,
            ),
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable(
                    onClick = { onClick(recruitment.id) },
                    enabled = recruitment.id != 0L,
                ),
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        color = if (recruitment.companyProfileUrl.isEmpty()) {
                            JobisTheme.colors.surfaceVariant
                        } else {
                            Color.Unspecified
                        },
                    ),
                model = recruitment.companyProfileUrl,
                contentDescription = "company profile",
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                JobisText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            color = if (recruitment.companyName.isBlank()) {
                                JobisTheme.colors.surfaceVariant
                            } else {
                                Color.Unspecified
                            },
                        ),
                    text = recruitment.companyName,
                    style = JobisTypography.SubHeadLine,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                JobisText(
                    modifier = Modifier
                        .fillMaxWidth(DEFAULT_SIZE_WHETHER_MILITARY_SUPPORTED)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (whetherMilitarySupported.isBlank()) {
                                JobisTheme.colors.surfaceVariant
                            } else {
                                Color.Unspecified
                            },
                        ),
                    text = whetherMilitarySupported,
                    style = JobisTypography.SubBody,
                    color = JobisTheme.colors.inverseOnSurface,
                )
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        JobisIconButton(
            modifier = Modifier.padding(4.dp),
            painter = painterResource(id = bookmarkIcon),
            contentDescription = "bookmark",
            onClick = { onBookmarked(recruitment.id) },
            tint = JobisTheme.colors.onPrimary,
        )
    }
}
