package team.retum.jobis.recruitment.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.retum.common.model.ApplicationData
import team.retum.jobis.recruitment.R
import team.retum.jobis.recruitment.viewmodel.RecruitmentDetailsSideEffect
import team.retum.jobis.recruitment.viewmodel.RecruitmentDetailsViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.card.JobisCard
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.usecase.entity.AreasEntity
import team.retum.usecase.entity.RecruitmentDetailsEntity
import java.net.URLEncoder

@Composable
internal fun RecruitmentDetails(
    recruitmentId: Long,
    onBackPressed: () -> Unit,
    onApplyClick: (ApplicationData) -> Unit,
    navigateToCompanyDetails: (Long, Boolean) -> Unit,
    isMovedCompanyDetails: Boolean,
    recruitmentDetailsViewModel: RecruitmentDetailsViewModel = hiltViewModel(),
) {
    val state by recruitmentDetailsViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        recruitmentDetailsViewModel.fetchRecruitmentDetails(recruitmentId)
        recruitmentDetailsViewModel.sideEffect.collect {
            when (it) {
                is RecruitmentDetailsSideEffect.BadRequest -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.occurred_error),
                        drawable = JobisIcon.Error,
                    ).show()
                }

                is RecruitmentDetailsSideEffect.MoveToCompanyDetails -> {
                    navigateToCompanyDetails(it.companyId, true)
                }
            }
        }
    }

    RecruitmentDetailsScreen(
        onBackPressed = onBackPressed,
        onApplyClick = onApplyClick,
        onBookmarkClick = { recruitmentDetailsViewModel.bookmarkRecruitmentDetail(recruitmentId) },
        recruitmentDetail = state.recruitmentDetailsEntity,
        recruitmentId = recruitmentId,
        onMoveToCompanyDetailsClick = recruitmentDetailsViewModel::onMoveToCompanyDetailsClick,
        isMovedCompanyDetails = isMovedCompanyDetails,
    )
}

@Composable
private fun RecruitmentDetailsScreen(
    onBackPressed: () -> Unit,
    onApplyClick: (ApplicationData) -> Unit,
    onBookmarkClick: () -> Unit,
    recruitmentDetail: RecruitmentDetailsEntity,
    recruitmentId: Long,
    onMoveToCompanyDetailsClick: () -> Unit,
    isMovedCompanyDetails: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(onBackPressed = onBackPressed)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            CompanyInformation(
                recruitmentDetail = recruitmentDetail,
                onMoveToCompanyDetailsClick = onMoveToCompanyDetailsClick,
                isMovedCompanyDetails = isMovedCompanyDetails,
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp),
                color = JobisTheme.colors.inverseSurface,
                thickness = 1.dp,
            )
            RecruitmentDetailInfo(recruitmentDetail = recruitmentDetail)
        }
        BottomBar(
            onApplyClick = {
                onApplyClick(
                    ApplicationData(
                        applicationId = 0,
                        recruitmentId = recruitmentId,
                        rejectionReason = "",
                        companyLogoUrl = URLEncoder.encode(
                            recruitmentDetail.companyProfileUrl,
                            "UTF8",
                        ),
                        companyName = recruitmentDetail.companyName,
                        isReApply = false,
                    ),
                )
            },
            onBookmarkClick = onBookmarkClick,
            isBookmark = recruitmentDetail.bookmarked,
            isApplicable = recruitmentDetail.isApplicable,
        )
    }
}

@Composable
private fun CompanyInformation(
    recruitmentDetail: RecruitmentDetailsEntity,
    onMoveToCompanyDetailsClick: () -> Unit,
    isMovedCompanyDetails: Boolean,
) {
    Row(
        modifier = Modifier.padding(
            horizontal = 24.dp,
            vertical = 12.dp,
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(8.dp))
                .size(64.dp)
                .border(
                    width = 1.dp,
                    color = JobisTheme.colors.inverseSurface,
                    shape = RoundedCornerShape(8.dp),
                ),
            model = recruitmentDetail.companyProfileUrl,
            contentDescription = "company profile",
            contentScale = ContentScale.Crop,
        )
        JobisText(
            text = recruitmentDetail.companyName,
            style = JobisTypography.HeadLine,
        )
    }
    JobisButton(
        text = stringResource(id = R.string.show_company_detail_info),
        onClick = onMoveToCompanyDetailsClick,
        enabled = !isMovedCompanyDetails,
    )
}

@Composable
private fun RecruitmentDetailInfo(
    recruitmentDetail: RecruitmentDetailsEntity,
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        recruitmentDetail.apply {
            Detail(
                title = stringResource(id = R.string.recruitment_period),
                content = getRecruitmentPeriod(
                    startDate = startDate,
                    endDate = endDate,
                ),
            )
            if (!winterIntern && militarySupport != null) {
                Detail(
                    title = stringResource(id = R.string.special_military_service_whether),
                    content = "병역특례 ${if (militarySupport!!) "" else "불"}가능",
                )
            }
            Position(areas = recruitmentDetail.areas.toPersistentList())
            Detail(
                title = stringResource(id = R.string.certificate),
                content = exceptBracket(requiredLicenses.toString()),
            )
            Detail(
                title = stringResource(id = if (winterIntern) R.string.selection_procedures else R.string.recruitment_procedure),
                content = StringBuilder().apply {
                    hiringProgress.forEachIndexed { index, it ->
                        append("${index + 1}. ${it.value}")
                        if (index != hiringProgress.lastIndex) {
                            append("\n")
                        }
                    }
                }.toString(),
            )
            Detail(
                title = stringResource(id = R.string.additional_qualifications),
                content = additionalQualifications,
            )
            Detail(
                title = stringResource(id = R.string.work_hours),
                content = workingHours,
            )
            Detail(
                title = stringResource(id = R.string.wage_and_benefits),
                content = benefits,
            )
            Detail(
                title = stringResource(id = R.string.submission_document),
                content = submitDocument,
            )
            Detail(
                title = stringResource(id = R.string.etc),
                content = etc,
            )
            if (winterIntern && integrationPlan != null) {
                Detail(
                    title = stringResource(id = R.string.integration_plan),
                    content = "${if (integrationPlan!!) "있" else "없"}음",
                )
            }
            if (!winterIntern && hireConvertible != null) {
                Detail(
                    title = stringResource(id = R.string.hireConvertible),
                    content = "${if (hireConvertible!!) "있" else "없"}음",
                )
            }
        }
    }
}

@Composable
internal fun Detail(
    title: String,
    content: String?,
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        JobisText(
            modifier = Modifier.padding(vertical = 8.dp),
            text = title,
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        JobisText(
            modifier = Modifier.padding(vertical = 4.dp),
            text = if (content.isNullOrEmpty() || content == "null") "-" else content,
            style = JobisTypography.Body,
        )
    }
}

@Composable
private fun Position(
    areas: ImmutableList<AreasEntity>,
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        JobisText(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(id = R.string.job_position),
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        if (areas.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                areas.forEach { areas ->
                    PositionCard(
                        job = areas.job.map { it.name }.toPersistentList(),
                        majorTask = areas.majorTask,
                        tech = areas.tech.map { it.name }.toPersistentList(),
                        preferentialTreatment = areas.preferentialTreatment,
                    )
                }
            }
        }
    }
}

@Composable
private fun PositionCard(
    job: ImmutableList<String>,
    majorTask: String,
    tech: ImmutableList<String>,
    preferentialTreatment: String?,
) {
    var showDetails by remember { mutableStateOf(false) }
    val iconRotate by animateFloatAsState(
        targetValue = if (showDetails) {
            180f
        } else {
            0f
        },
        label = "",
    )

    JobisCard(onClick = { showDetails = !showDetails }) {
        Column {
            Row(
                modifier = Modifier.padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 4.dp,
                    alignment = Alignment.Start,
                ),
            ) {
                JobisText(
                    modifier = Modifier.weight(1f),
                    text = exceptBracket(job.toString()),
                    style = JobisTypography.SubHeadLine,
                )
                Icon(
                    modifier = Modifier.rotate(iconRotate),
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    tint = JobisTheme.colors.onSurfaceVariant,
                    contentDescription = "detail",
                )
            }
            AnimatedVisibility(visible = showDetails) {
                Column {
                    PositionDetail(
                        title = stringResource(id = R.string.major_task),
                        content = majorTask,
                    )
                    PositionDetail(
                        title = stringResource(id = R.string.technology_used),
                        content = exceptBracket(tech.toString()),
                    )
                    PositionDetail(
                        title = stringResource(id = R.string.preferential_treatment),
                        content = preferentialTreatment,
                    )
                }
            }
        }
    }
}

@Composable
internal fun PositionDetail(
    title: String,
    content: String?,
) {
    Column(
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp,
            ),
    ) {
        JobisText(
            text = title,
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        JobisText(
            modifier = Modifier.padding(top = 4.dp),
            text = if (content.isNullOrEmpty()) "-" else content,
            style = JobisTypography.Body,
        )
    }
}

internal fun exceptBracket(text: String): String {
    return text
        .replace("[", "")
        .replace("]", "")
        .trim()
}

@Composable
private fun BottomBar(
    onApplyClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    isBookmark: Boolean,
    isApplicable: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = JobisTheme.colors.background)
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
    ) {
        Button(
            modifier = Modifier.weight(1f),
            enabled = isApplicable,
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = JobisTheme.colors.onPrimary,
                contentColor = JobisTheme.colors.background,
                disabledContainerColor = JobisTheme.colors.surfaceTint,
                disabledContentColor = JobisTheme.colors.background,
            ),
            onClick = onApplyClick,
        ) {
            JobisText(
                text = stringResource(
                    id = if (isApplicable) {
                        R.string.apply
                    } else {
                        R.string.can_do_not_apply
                    },
                ),
                style = JobisTypography.SubHeadLine,
                color = Color.White,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = JobisIcon.LongArrow),
                contentDescription = "long arrow",
                tint = Color.White,
            )
        }
        Icon(
            modifier = Modifier
                .clickable(onClick = onBookmarkClick)
                .background(
                    color = JobisTheme.colors.inverseSurface,
                    shape = RoundedCornerShape(12.dp),
                )
                .clip(RectangleShape)
                .padding(16.dp),
            painter = painterResource(
                if (isBookmark) {
                    JobisIcon.BookmarkOn
                } else {
                    JobisIcon.BookmarkOff
                },
            ),
            contentDescription = "bookmark",
            tint = if (isBookmark) {
                JobisTheme.colors.onPrimary
            } else {
                JobisTheme.colors.onSurfaceVariant
            },
        )
    }
}

private fun getRecruitmentPeriod(
    startDate: String?,
    endDate: String?,
): String {
    if (startDate == null && endDate == null) {
        return "상시 모집"
    }
    return "$startDate ~ $endDate"
}
