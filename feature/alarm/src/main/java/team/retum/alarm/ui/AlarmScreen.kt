package team.retum.alarm.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.retum.alarm.R
import team.returm.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.returm.jobisdesignsystemv2.foundation.JobisTheme
import team.returm.jobisdesignsystemv2.foundation.JobisTypography

// TODO 서버 연동 시 제거
private data class AlarmData(
    val companyName: String,
    val content: String,
    val date: String,
)

@Composable
internal fun Alarm(
    onBackPressed: () -> Unit,
) {
    val alarmList = emptyList<AlarmData>()
    AlarmScreen(
        onBackPressed = onBackPressed,
        alarmList = alarmList,
    )
}

@Composable
private fun AlarmScreen(
    onBackPressed: () -> Unit,
    alarmList: List<AlarmData>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background)
            .padding(horizontal = 24.dp),
    ) {
        JobisSmallTopAppBar(
            title = stringResource(id = R.string.alarm),
            onBackPressed = onBackPressed,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            alarmList.forEach {
                AlarmContent(
                    companyName = it.companyName,
                    content = it.content,
                    date = it.date,
                )
            }
        }
    }
}

@Composable
private fun AlarmContent(
    companyName: String,
    content: String,
    date: String,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(0.14f)
            .padding(vertical = 16.dp),
    ) {
        Text(
            text = companyName,
            style = JobisTypography.Description,
            color = JobisTheme.colors.onPrimary,
        )
        Text(
            text = content,
            style = JobisTypography.HeadLine,
            color = JobisTheme.colors.onBackground,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = date,
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
    }
}