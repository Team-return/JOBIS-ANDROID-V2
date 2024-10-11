package team.retum.notification.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.retum.alarm.R
import team.retum.jobisdesignsystemv2.appbar.JobisSmallTopAppBar
import team.retum.jobisdesignsystemv2.empty.EmptyContent
import team.retum.jobisdesignsystemv2.foundation.JobisTheme
import team.retum.jobisdesignsystemv2.foundation.JobisTypography
import team.retum.jobisdesignsystemv2.tab.TabBar
import team.retum.jobisdesignsystemv2.text.JobisText
import team.retum.jobisdesignsystemv2.utils.clickable
import team.retum.notification.model.NotificationDetailData
import team.retum.notification.viewmodel.NotificationsSideEffect
import team.retum.notification.viewmodel.NotificationsViewModel
import team.retum.usecase.entity.notification.NotificationsEntity

@Composable
internal fun Notifications(
    onBackPressed: () -> Unit,
    navigateToRecruitment: (Long) -> Unit,
    navigateToHome: (Long) -> Unit,
    notificationsViewModel: NotificationsViewModel = hiltViewModel(),
) {
    val state by notificationsViewModel.state.collectAsStateWithLifecycle()
    val tabs = listOf(
        stringResource(id = R.string.all),
        stringResource(id = R.string.read),
        stringResource(id = R.string.not_read),
    )

    LaunchedEffect(notificationsViewModel.sideEffect) {
        notificationsViewModel.sideEffect.collect {
            when (it) {
                is NotificationsSideEffect.MoveToRecruitment -> {
                    navigateToRecruitment(it.recruitmentId)
                }

                is NotificationsSideEffect.MoveToHome -> {
                    navigateToHome(it.applicationId)
                }
            }
        }
    }
    LaunchedEffect(state.selectedTabIndex) {
        when (state.selectedTabIndex) {
            0 -> notificationsViewModel.setIsNew(isNew = null)
            1 -> notificationsViewModel.setIsNew(isNew = false)
            2 -> notificationsViewModel.setIsNew(isNew = true)
        }
        notificationsViewModel.fetchNotifications()
    }

    NotificationsScreen(
        onBackPressed = onBackPressed,
        notificationList = notificationsViewModel.notifications,
        selectedTabIndex = state.selectedTabIndex,
        tabs = tabs.toPersistentList(),
        onSelectTab = { notificationsViewModel.setTabIndex(it) },
        onNotificationDetailsClick = notificationsViewModel::readNotification,
    )
}

@Composable
private fun NotificationsScreen(
    onBackPressed: () -> Unit,
    notificationList: SnapshotStateList<NotificationsEntity.NotificationEntity>,
    selectedTabIndex: Int,
    tabs: ImmutableList<String>,
    onSelectTab: (Int) -> Unit,
    onNotificationDetailsClick: (NotificationDetailData) -> Unit,
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
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(notificationList) {
                NotificationContent(
                    notifications = it,
                    companyName = it.title,
                    content = it.content,
                    date = it.createAt,
                    isNew = it.new,
                    onClick = onNotificationDetailsClick,
                )
            }
        }
        if (notificationList.isEmpty()) {
            EmptyContent(title = stringResource(id = R.string.no_notification))
        }
    }
}

@Composable
private fun NotificationContent(
    notifications: NotificationsEntity.NotificationEntity,
    companyName: String,
    content: String,
    date: String,
    isNew: Boolean,
    onClick: (NotificationDetailData) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isNew) {
                    JobisTheme.colors.secondaryContainer
                } else {
                    JobisTheme.colors.background
                },
            )
            .padding(
                vertical = 16.dp,
                horizontal = 24.dp,
            )
            .clickable(
                enabled = true,
                onClick = {
                    onClick(
                        NotificationDetailData(
                            isNew = notifications.new,
                            notificationId = notifications.notificationId,
                            detailId = notifications.detailId,
                            topic = notifications.topic,
                        ),
                    )
                },
            ),
    ) {
        JobisText(
            text = companyName,
            style = JobisTypography.Description,
            color = JobisTheme.colors.onPrimary,
        )
        JobisText(
            text = content,
            style = JobisTypography.HeadLine,
            color = JobisTheme.colors.onBackground,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement
                .spacedBy(
                    space = 4.dp,
                    alignment = Alignment.Start,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            JobisText(
                text = date,
                style = JobisTypography.Description,
                color = JobisTheme.colors.onSurfaceVariant,
            )
            if (isNew) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(
                            shape = CircleShape,
                            color = JobisTheme.colors.error,
                        ),
                )
            }
        }
    }
}
