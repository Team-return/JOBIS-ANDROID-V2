package team.retum.jobis.recruitment.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import team.retum.common.model.ReApplyData
import team.retum.jobis.recruitment.R
import team.retum.jobis.recruitment.viewmodel.RecruitmentDetailsSideEffect
import team.retum.jobis.recruitment.viewmodel.RecruitmentDetailsViewModel
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.usecase.entity.AreasEntity
import team.retum.usecase.entity.RecruitmentDetailsEntity

@Composable
internal fun RecruitmentDetails(
    recruitmentId: Long,
    onBackPressed: () -> Unit,
    onApplyClick: (ReApplyData) -> Unit,
    navigateToCompanyDetails: (Long) -> Unit,
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
                    navigateToCompanyDetails(it.companyId)
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
    )
}

@Composable
private fun RecruitmentDetailsScreen(
    onBackPressed: () -> Unit,
    onApplyClick: (ReApplyData) -> Unit,
    onBookmarkClick: () -> Unit,
    recruitmentDetail: RecruitmentDetailsEntity,
    recruitmentId: Long,
    onMoveToCompanyDetailsClick: () -> Unit,
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
            )
            Divider(
                modifier = Modifier.padding(horizontal = 24.dp),
                color = JobisTheme.colors.inverseSurface,
                thickness = 1.dp,
            )
            RecruitmentDetailInfo(recruitmentDetail = recruitmentDetail)
        }
        BottomBar(
            onApplyClick = {
                onApplyClick(
                    ReApplyData(
                        recruitmentId = recruitmentId,
                        companyLogoUrl = recruitmentDetail.companyProfileUrl.replace("/", " "),
                        companyName = recruitmentDetail.companyName,
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
            contentScale = ContentScale.FillBounds,
        )
        JobisText(
            text = recruitmentDetail.companyName,
            style = JobisTypography.HeadLine,
        )
    }
    JobisButton(
        text = stringResource(id = R.string.show_company_detail_info),
        onClick = onMoveToCompanyDetailsClick,
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
                content = "$startDate${if (startDate.isNotBlank()) " ~ " else ""}$endDate",
            )
            Detail(
                title = stringResource(id = R.string.special_military_service_whether),
                content = "병역특례 ${if (military) "" else "불"}가능",
            )
            Position(areas = recruitmentDetail.areas)
            Detail(
                title = stringResource(id = R.string.certificate),
                content = exceptBracket(requiredLicenses.toString()),
            )
            Detail(
                title = stringResource(id = R.string.recruitment_procedure),
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
                title = stringResource(id = R.string.required_grade),
                content = requiredGrade.toString(),
            )
            Detail(
                title = stringResource(id = R.string.work_hours),
                content = workingHours,
            )
            Detail(
                title = stringResource(id = R.string.train_pay),
                content = "$trainPay 만원/월",
            )
            Detail(
                title = stringResource(id = R.string.convert_to_full_time_job),
                content = "$pay 만원/년",
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
            text = if (content.isNullOrEmpty()) "-" else content,
            style = JobisTypography.Body,
        )
    }
}

@Composable
private fun Position(
    areas: List<AreasEntity>,
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        JobisText(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(id = R.string.job_position),
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        if (areas.isNotEmpty()) {
            areas.forEach {
                PositionCard(
                    job = exceptBracket(it.job.toString()).replace(",", "/")
                        .replace(",", "/"),
                    majorTask = it.majorTask,
                    tech = exceptBracket(it.tech.toString()),
                    preferentialTreatment = it.preferentialTreatment,
                )
            }
        }
    }
}

@Composable
private fun PositionCard(
    job: String,
    majorTask: String,
    tech: String,
    preferentialTreatment: String?,
) {
    var showDetails by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(JobisTheme.colors.inverseSurface),
    ) {
        Row(
            modifier = Modifier.padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
        ) {
            JobisText(
                modifier = Modifier.weight(1f),
                text = job,
                style = JobisTypography.SubHeadLine,
            )
            JobisIconButton(
                defaultBackgroundColor = JobisTheme.colors.inverseSurface,
                painter = painterResource(
                    id = if (showDetails) {
                        R.drawable.ic_arrow_up
                    } else {
                        R.drawable.ic_arrow_down
                    },
                ),
                onClick = { showDetails = !showDetails },
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
                    content = tech,
                )
                PositionDetail(
                    title = stringResource(id = R.string.preferential_treatment),
                    content = preferentialTreatment,
                )
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
                text = if (isApplicable) {
                    stringResource(id = R.string.apply)
                } else {
                    stringResource(id = R.string.can_do_apply_third)
                },
                style = JobisTypography.SubHeadLine,
                color = JobisTheme.colors.background,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = JobisIcon.LongArrow),
                contentDescription = "long arrow",
            )
        }
        Icon(
            modifier = Modifier
                .background(
                    color = JobisTheme.colors.inverseSurface,
                    shape = RoundedCornerShape(12.dp),
                )
                .clip(RectangleShape)
                .padding(16.dp)
                .clickable(
                    enabled = true,
                    onClick = onBookmarkClick,
                ),
            painter = painterResource(
                if (isBookmark) JobisIcon.BookmarkOn else JobisIcon.BookmarkOff,
            ),
            contentDescription = "bookmark",
        )
    }
}
