package team.retum.notification.ui

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import team.retum.alarm.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.tab.TabBar
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.notification.viewmodel.NotificationsViewModel
import team.retum.usecase.entity.notification.NotificationsEntity

@Composable
internal fun Notification(
    onBackPressed: () -> Unit,
    onNotificationDetailsClick: (Long) -> Unit,
    notificationsViewModel: NotificationsViewModel = hiltViewModel(),
) {
    val state by notificationsViewModel.state.collectAsStateWithLifecycle()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(id = R.string.all),
        stringResource(id = R.string.read),
        stringResource(id = R.string.not_read),
    )

    LaunchedEffect(Unit) {
        when (selectedTabIndex) {
            1 -> state.isNew ?: true
            2 -> state.isNew ?: false
        }
        notificationsViewModel.fetchNotifications()
    }

    NotificationsScreen(
        onBackPressed = onBackPressed,
        notificationList = notificationsViewModel.notifications,
        selectedTabIndex = selectedTabIndex,
        tabs = tabs,
        onSelectTab = { selectedTabIndex = it },
        onNotificationDetailsClick = onNotificationDetailsClick,
    )
}

@Composable
private fun NotificationsScreen(
    onBackPressed: () -> Unit,
    notificationList: SnapshotStateList<NotificationsEntity.NotificationEntity>,
    selectedTabIndex: Int,
    tabs: List<String>,
    onSelectTab: (Int) -> Unit,
    onNotificationDetailsClick: (Long) -> Unit,
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
            items(notificationList) {
                NotificationContent(
                    notifications = it,
                    companyName = it.title,
                    content = it.content,
                    date = it.createAt,
                    onClick = onNotificationDetailsClick,
                )
            }
        }
    }
}

@Composable
private fun NotificationContent(
    notifications: NotificationsEntity.NotificationEntity,
    companyName: String,
    content: String,
    date: String,
    onClick: (notificationId: Long) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(0.14f)
            .padding(vertical = 16.dp)
            .clickable(
                enabled = true,
                onClick = { onClick(notifications.notificationId) },
            ),
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
