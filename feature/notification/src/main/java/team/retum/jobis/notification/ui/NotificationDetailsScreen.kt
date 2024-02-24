package team.retum.jobis.notification.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.retum.jobis.notification.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.button.JobisIconButton
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.text.JobisText


@Composable
internal fun NotificationDetails(
    onBackPressed: () -> Unit,
) {
    NotificationDetailsScreen(
        onBackPressed = onBackPressed,
        scrollState = rememberScrollState(),
    )
}

@Composable
private fun NotificationDetailsScreen(
    onBackPressed: () -> Unit,
    scrollState: ScrollState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(
            title = stringResource(id = R.string.announcement),
            onBackPressed = onBackPressed,
        )
        Column(
            Modifier
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
        ) {
            Notice()
            AttachFile()
        }
    }
}

@Composable
fun Notice() {
    Column(modifier = Modifier.padding(vertical = 24.dp)) {
        JobisText(
            text = "[중요] 신입생 오레인테이션 안내",
            style = JobisTypography.HeadLine,
            color = JobisTheme.colors.onSurface,
        )
        JobisText(
            modifier = Modifier.padding(top = 4.dp),
            text = "2023-12-05",
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        JobisText(
            modifier = Modifier.padding(top = 16.dp),
            text = "신입생 오리엔테이션 책자에 있는 입학전 과제의 양식입니다.\n" +
                    "첨부파일을 다운받아 사용하시고, \n" +
                    "영어와 전공은 특별한 양식이 없으니 내용에 맞게 작성하여 학교 홈페이지\n" +
                    "에 제출하시기 바랍니다.\n" +
                    " \n" +
                    "■ 과제 제출 마감: 2024년 2월 20일 화요일\n" +
                    "■ 학교 홈페이지 학생 회원가입 -> 학교 담당자가 승인\n" +
                    "■ 학교 홈페이지 로그인 후 [과제제출 – 신입생 - 각 교과] 게시판에 제출\n" +
                    "■ 과제 중 자기소개 PPT는 첨부한 파일을 참고하되, 자유롭게 만들어도 \n" +
                    "됩니다.",
            style = JobisTypography.Body,
            color = JobisTheme.colors.onSurface,
        )
    }
}

@Composable
fun AttachFile() {
    Column {
        JobisText(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(id = R.string.attachFile),
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            JobisText(
                modifier = Modifier.padding(start = 12.dp),
                text = "2024학년도 신입생 과제.hwp",
                style = JobisTypography.Body,
            )
            JobisIconButton(
                painter = painterResource(id = R.drawable.ic_download),
                contentDescription = "download",
                onClick = {},
            )
        }
    }
}
