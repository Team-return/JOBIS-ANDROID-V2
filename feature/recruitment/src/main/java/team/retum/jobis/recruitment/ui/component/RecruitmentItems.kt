package team.retum.jobis.recruitment.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import team.retum.common.enums.MilitarySupport
import team.retum.jobis.recruitment.R
import team.retum.jobis.recruitment.model.RecruitmentStatus
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.usecase.entity.RecruitmentsEntity
import java.time.LocalDate

private const val DEFAULT_SIZE_WHETHER_MILITARY_SUPPORTED = 0.6f

@Composable
internal fun RecruitmentItems(
    recruitments: ImmutableList<RecruitmentsEntity.RecruitmentEntity>,
    onRecruitmentClick: (Long) -> Unit,
    onBookmarkClick: (Long) -> Unit,
    whetherFetchNextPage: (Int) -> Boolean,
    fetchNextPage: () -> Unit,
) {
    LazyColumn {
        items(
            count = recruitments.size,
            key = { index -> index },
        ) { index ->
            var bookmarked = recruitments[index].bookmarked
            RecruitmentItem(
                recruitment = recruitments[index],
                onClick = onRecruitmentClick,
                bookmarked = bookmarked,
                onBookmarked = { id ->
                    onBookmarkClick(id)
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
    bookmarked: Boolean,
    onBookmarked: (recruitId: Long) -> Unit,
) {
    val whetherMilitarySupported = when (recruitment.militarySupport) {
        MilitarySupport.TRUE -> stringResource(id = R.string.military_supported)
        MilitarySupport.FALSE -> stringResource(id = R.string.military_not_supported)
        else -> ""
    }
    val (recruitmentStatus, statusTextColor, backgroundColor, borderColor) = when (recruitment.status) {
        "RECRUITING" -> RecruitmentStatus("모집 중", JobisTheme.colors.onPrimary, JobisTheme.colors.background, JobisTheme.colors.onPrimary)
        "DONE" -> RecruitmentStatus("모집 종료", JobisTheme.colors.onSurfaceVariant, JobisTheme.colors.inverseSurface, JobisTheme.colors.surfaceTint)
        else ->  RecruitmentStatus("", JobisTheme.colors.onPrimary, JobisTheme.colors.background, JobisTheme.colors.onPrimary)
    }
    val currentYear = LocalDate.now().toString()

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
                contentScale = ContentScale.Crop,
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
                Row {
                    JobisText(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (recruitment.militarySupport == MilitarySupport.LOADING) {
                                    JobisTheme.colors.surfaceVariant
                                } else {
                                    Color.Unspecified
                                },
                            ),
                        text = whetherMilitarySupported,
                        style = JobisTypography.SubBody,
                        color = JobisTheme.colors.inverseOnSurface,
                    )
                    if (recruitment.year == currentYear.take(4).toInt()) {
                        recruitment.apply {
                            JobisText(
                                text = " • ",
                                style = JobisTypography.SubBody,
                                color = JobisTheme.colors.inverseOnSurface,
                            )
                            JobisText(
                                text = "${recruitment.year}",
                                style = JobisTypography.SubBody,
                                color = JobisTheme.colors.onPrimary,
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(100.dp)
                )
        ) {
            JobisText(
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp),
                text = recruitmentStatus,
                style = JobisTypography.Description,
                color = statusTextColor,
            )
        }
        JobisIconButton(
            modifier = Modifier.padding(vertical = 8.dp),
            drawableResId = if (bookmarked) {
                JobisIcon.BookmarkOn
            } else {
                JobisIcon.BookmarkOff
            },
            contentDescription = "bookmark",
            onClick = { onBookmarked(recruitment.id) },
            tint = if (bookmarked) {
                JobisTheme.colors.onPrimary
            } else {
                JobisTheme.colors.onSurfaceVariant
            },
        )
    }
}
