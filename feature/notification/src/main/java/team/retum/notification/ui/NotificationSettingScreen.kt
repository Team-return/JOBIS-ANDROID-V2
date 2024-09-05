package team.retum.notification.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import team.retum.alarm.R
import team.retum.common.enums.NotificationTopic
import team.retum.jobisdesignsystemv2.appbar.JobisCollapsingTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.switches.JobisSwitch
import team.retum.jobisdesignsystemv2.toast.JobisToast
import team.retum.notification.viewmodel.NotificationSettingSideEffect
import team.retum.notification.viewmodel.NotificationSettingState
import team.retum.notification.viewmodel.NotificationSettingViewModel

@Composable
internal fun NotificationSetting(
    onBackPressed: () -> Unit,
    notificationSettingViewModel: NotificationSettingViewModel = hiltViewModel(),
) {
    val state by notificationSettingViewModel.state.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        notificationSettingViewModel.sideEffect.collect {
            when (it) {
                is NotificationSettingSideEffect.CanNotCurrentNotificationStatus -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.cannot_current_notification_status),
                    ).show()
                }

                is NotificationSettingSideEffect.ChangeNotificationFailure -> {
                    JobisToast.create(
                        context = context,
                        message = context.getString(R.string.change_notification_fail),
                    ).show()
                }
            }
        }
    }

    NotificationSettingScreen(
        onBackPressed = onBackPressed,
        state = state,
        scrollState = scrollState,
        onAllNotificationChange = notificationSettingViewModel::setAllNotification,
        onTopicChange = notificationSettingViewModel::setNotificationTopic,
    )
}

@Composable
private fun NotificationSettingScreen(
    onBackPressed: () -> Unit,
    state: NotificationSettingState,
    scrollState: ScrollState,
    onAllNotificationChange: (Boolean) -> Unit,
    onTopicChange: (NotificationTopic, Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JobisTheme.colors.background),
    ) {
        JobisCollapsingTopAppBar(
            title = stringResource(id = R.string.notification_setting),
            onBackPressed = onBackPressed,
            scrollState = scrollState,
        )
        NotificationLayout(
            isSubscribe = state.isAllSubscribe,
            onCheckChange = onAllNotificationChange,
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 12.dp,
                )
                .height(1.dp)
                .background(JobisTheme.colors.surfaceVariant),
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 8.dp,
                ),
            text = stringResource(id = R.string.detail_notification),
            style = JobisTypography.Description,
            color = JobisTheme.colors.onSurfaceVariant,
        )
        NotificationDetailLayout(
            title = stringResource(id = R.string.notice_notification),
            topic = NotificationTopic.NEW_NOTICE,
            isSubscribe = state.isNoticeSubscribe,
            onCheckChange = onTopicChange,
        )
        NotificationDetailLayout(
            title = stringResource(id = R.string.application_notification),
            topic = NotificationTopic.APPLICATION_STATUS_CHANGED,
            isSubscribe = state.isApplicationSubscribe,
            onCheckChange = onTopicChange,
        )
        NotificationDetailLayout(
            title = stringResource(id = R.string.recruitment_notification),
            topic = NotificationTopic.RECRUITMENT_DONE,
            isSubscribe = state.isRecruitmentSubscribe,
            onCheckChange = onTopicChange,
        )
    }
}

@Composable
private fun NotificationLayout(
    isSubscribe: Boolean,
    onCheckChange: (checked: Boolean) -> Unit,
) {
    var isChecked by remember { mutableStateOf(isSubscribe) }
    LaunchedEffect(isSubscribe) { isChecked = isSubscribe }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.all_notification),
            style = JobisTypography.Body,
            color = JobisTheme.colors.inverseOnSurface,
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisSwitch(
            checked = isChecked,
            onCheckedChange = { checked ->
                isChecked = checked
                onCheckChange(checked)
            },
        )
    }
}

@Composable
private fun NotificationDetailLayout(
    title: String,
    topic: NotificationTopic,
    isSubscribe: Boolean,
    onCheckChange: (NotificationTopic, Boolean) -> Unit,
) {
    var isChecked by remember { mutableStateOf(isSubscribe) }
    LaunchedEffect(isSubscribe) { isChecked = isSubscribe }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = JobisTypography.Body,
            color = JobisTheme.colors.inverseOnSurface,
        )
        Spacer(modifier = Modifier.weight(1f))
        JobisSwitch(
            checked = isChecked,
            onCheckedChange = { checked ->
                isChecked = checked
                onCheckChange(topic, isChecked)
            },
        )
    }
}
