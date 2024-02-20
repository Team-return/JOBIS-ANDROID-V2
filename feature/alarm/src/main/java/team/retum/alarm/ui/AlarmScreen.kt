package team.retum.alarm.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import team.retum.alarm.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.tab.TabBar

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
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(id = R.string.all),
        stringResource(id = R.string.read),
        stringResource(id = R.string.not_read),
    )
    val alarmList = listOf(
        AlarmData(
            "companyName",
            "contet",
            "date",
        ),
        AlarmData(
            "companyName",
            "contet",
            "date",
        ),
        AlarmData(
            "companyName",
            "contet",
            "date",
        ),
        AlarmData(
            "companyName",
            "contet",
            "date",
        ),
        AlarmData(
            "companyName",
            "contet",
            "date",
        ),
        AlarmData(
            "companyName",
            "contet",
            "date",
        ),
        AlarmData(
            "companyName",
            "contet",
            "date",
        ),
        AlarmData(
            "companyName",
            "contet",
            "date",
        ),
        AlarmData(
            "companyName",
            "contet",
            "date",
        ),
        AlarmData(
            "companyName",
            "contet",
            "date",
        ),
        AlarmData(
            "companyName",
            "contet",
            "date",
        ),
        AlarmData(
            "companyName",
            "contet",
            "date",
        ),
        AlarmData(
            "companyName",
            "contet",
            "date",
        ),
        AlarmData(
            "companyName",
            "contet",
            "date",
        ),
    )
    AlarmScreen(
        onBackPressed = onBackPressed,
        alarmList = alarmList,
        selectedTabIndex = selectedTabIndex,
        tabs = tabs,
        onSelectTab = { selectedTabIndex = it },
    )
}

@Composable
private fun AlarmScreen(
    onBackPressed: () -> Unit,
    alarmList: List<AlarmData>,
    selectedTabIndex: Int,
    tabs: List<String>,
    onSelectTab: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisSmallTopAppBar(
            title = stringResource(id = R.string.alarm),
            onBackPressed = onBackPressed,
        )
        TabBar(
            selectedTabIndex = selectedTabIndex,
            tabs = tabs,
            onSelectTab = onSelectTab,
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        ) {
            items(alarmList) {
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
