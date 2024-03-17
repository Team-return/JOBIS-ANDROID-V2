package team.retum.notification.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.AlarmType
import team.retum.notification.model.NotificationDetailData
import team.retum.usecase.entity.notification.NotificationsEntity
import team.retum.usecase.usecase.notification.FetchNotificationsUseCase
import team.retum.usecase.usecase.notification.ReadNotificationUseCase
import javax.inject.Inject

@HiltViewModel
internal class NotificationsViewModel @Inject constructor(
    private val fetchNotificationsUseCase: FetchNotificationsUseCase,
    private val readNotificationUseCase: ReadNotificationUseCase,
) : BaseViewModel<NotificationsState, NotificationsSideEffect>(NotificationsState.getDefaultState()) {

    private val _notifications: SnapshotStateList<NotificationsEntity.NotificationEntity> =
        mutableStateListOf()
    val notifications get() = _notifications

    internal fun fetchNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            with(state.value) {
                fetchNotificationsUseCase(isNew = isNew)
                    .onSuccess {
                        _notifications.clear()
                        _notifications.addAll(it.notifications)
                    }
            }
        }
    }

    internal fun readNotification(
        notificationDetailData: NotificationDetailData,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (notificationDetailData.isNew) readNotificationUseCase(notificationDetailData.notificationId)
            when (notificationDetailData.topic) {
                AlarmType.APPLICATION_STATUS_CHANGED -> {
                    postSideEffect(NotificationsSideEffect.MoveToHome(applicationId = notificationDetailData.detailId))
                }

                AlarmType.RECRUITMENT_DONE -> {
                    postSideEffect(NotificationsSideEffect.MoveToRecruitment(recruitmentId = notificationDetailData.detailId))
                }

                else -> {
                    // TODO: 현재 처리할 작업 없음
                }
            }
        }
    }

    internal fun setTabIndex(tabIndex: Int) {
        setState { state.value.copy(selectedTabIndex = tabIndex) }
    }

    internal fun setIsNew(isNew: Boolean?) {
        setState { state.value.copy(isNew = isNew) }
    }
}

internal data class NotificationsState(
    val isNew: Boolean?,
    val selectedTabIndex: Int,
) {
    companion object {
        fun getDefaultState() = NotificationsState(
            isNew = null,
            selectedTabIndex = 0,
        )
    }
}

internal sealed interface NotificationsSideEffect {
    data class MoveToRecruitment(val recruitmentId: Long) : NotificationsSideEffect
    data class MoveToHome(val applicationId: Long) : NotificationsSideEffect
}
