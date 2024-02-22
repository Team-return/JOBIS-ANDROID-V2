package team.retum.jobis.recruitment.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.retum.common.enums.HiringProgress
import team.retum.jobis.recruitment.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.ButtonColor
import team.retum.jobisdesignsystemv2.button.JobisButton
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisIcon
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable

// TODO: 서버 연동 시 제거
private data class RecruitmentDetail(
    val companyId: Long,
    val companyProfileUrl: String,
    val companyName: String,
    val areas: List<Area>,
    val requiredGrade: String?,
    val startTime: String,
    val endTime: String,
    val requiredLicenses: List<String>?,
    val hiringProgress: List<HiringProgress>,
    val trainPay: Long,
    val pay: String?,
    val benefits: String?,
    val military: Boolean,
    val submitDocument: String,
    val startDate: String,
    val endDate: String,
    val etc: String?,
)

// TODO: 서버 연동 시 제거
private data class Area(
    val id: Long,
    val job: List<String>,
    val tech: List<String>,
    val hiring: Long,
    val majorTask: String,
    val preferentialTreatment: String?,
)

@Composable
internal fun RecruitmentDetails(
    onBackPressed: () -> Unit,
) {
    // TODO: 서버 연동 시 제거
    val recruitmentDetail = RecruitmentDetail(
        companyId = 118,
        companyProfileUrl = "LOGO_IMAGE/logo_73155.jpg",
        companyName = "(주)네이션에이",
        areas = listOf(
            Area(
                id = 19,
                job = listOf("서버 개발자"),
                tech = listOf(
                    "SpringBoot",
                    "SpringSecurity",
                    "SpringBatch",
                    "SpringDataJpa",
                    "Docker",
                ),
                hiring = 1,
                majorTask = "토스 서버를 개발해요",
                preferentialTreatment = "TS에 대한 지식이 깊으신분",
            ),
            Area(
                id = 20,
                job = listOf("Android", "iOS"),
                tech = listOf("Swift", "Java"),
                hiring = 2,
                majorTask = "토스 앱을 개발해요",
                preferentialTreatment = "안드에 대한 지식이 깊으신분",
            ),
        ),
        requiredGrade = "",
        startTime = "08:00:00",
        endTime = "06:00:00",
        requiredLicenses = listOf("정보처리 기능사, 정보처리 기능사, 정보처리 기능서, 정보처리 기능사"),
        hiringProgress = listOf(
            HiringProgress.DOCUMENT,
            HiringProgress.TECH_INTERVIEW,
            HiringProgress.CODING_TEST,
        ),
        trainPay = 250,
        pay = "3000",
        benefits = "",
        military = false,
        submitDocument = "이력서, 포트폴리오",
        startDate = "2023-06-10",
        endDate = "20023-06-17",
        etc = "",
    )
    RecruitmentDetailScreen(
        onBackPressed = onBackPressed,
        recruitmentDetail = recruitmentDetail,
    )
}

@Composable
private fun RecruitmentDetailScreen(
    onBackPressed: () -> Unit,
    recruitmentDetail: RecruitmentDetail,
) {
    val scrollState = rememberScrollState()

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
                .verticalScroll(scrollState),
        ) {
            CompanyInformation(recruitmentDetail = recruitmentDetail)
            Divider(
                modifier = Modifier.padding(horizontal = 24.dp),
                color = JobisTheme.colors.inverseSurface,
                thickness = 1.dp,
            )
            RecruitmentDetailInfo(recruitmentDetail = recruitmentDetail)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(color = JobisTheme.colors.background),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            JobisButton(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.apply),
                color = ButtonColor.Primary,
                onClick = { },
            )
            Icon(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(56.dp)
                    .clip(RectangleShape)
                    .clickable(
                        enabled = true,
                        onPressed = { },
                        onClick = { },
                    )
                    .padding(
                        start = 4.dp,
                        top = 12.dp,
                        bottom = 12.dp,
                        end = 24.dp,
                    )
                    .background(
                        color = JobisTheme.colors.inverseSurface,
                        shape = RoundedCornerShape(12.dp),
                    ),
                painter = painterResource(id = JobisIcon.BookmarkOn),
                contentDescription = "bookmark",
            )
        }
    }
}

@Composable
private fun CompanyInformation(
    recruitmentDetail: RecruitmentDetail,
) {
    Row(
        modifier = Modifier.padding(
            horizontal = 24.dp,
            vertical = 12.dp,
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Image(
            modifier = Modifier
                .size(64.dp)
                .border(
                    width = 1.dp,
                    color = JobisTheme.colors.inverseSurface,
                    shape = RoundedCornerShape(12.dp),
                ),
            painter = painterResource(JobisIcon.Information),
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
        onClick = { },
    )
}

@Composable
private fun RecruitmentDetailInfo(
    recruitmentDetail: RecruitmentDetail,
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
                content = requiredGrade,
            )
            Detail(
                title = stringResource(id = R.string.work_hours),
                content = "$startTime ~ $endTime",
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
    areas: List<Area>,
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
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            JobisText(
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
