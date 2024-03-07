package team.retum.notification.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.notification.NotificationsEntity
import team.retum.usecase.usecase.notification.FetchNotificationsUseCase
import team.retum.usecase.usecase.notification.NotificationUseCase
import javax.inject.Inject

@HiltViewModel
internal class NotificationsViewModel @Inject constructor(
    private val fetchNotificationsUseCase: FetchNotificationsUseCase,
    private val notificationUseCase: NotificationUseCase,
) : BaseViewModel<NotificationsState, Unit>(NotificationsState.getDefaultState()) {

    internal var notifications: SnapshotStateList<NotificationsEntity.NotificationEntity> =
        mutableStateListOf()
        private set

    internal fun fetchNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            with(state.value) {
                fetchNotificationsUseCase(isNew = isNew)
                    .onSuccess {
                        notifications.addAll(it.notifications)
                    }
            }
        }
    }

    internal fun readNotification(notificationId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            notificationUseCase(notificationId)
        }
    }
}

internal data class NotificationsState(
    val isNew: Boolean,
    val selectedTabIndex: Int,
) {
    companion object {
        fun getDefaultState() = NotificationsState(
            isNew = false,
            selectedTabIndex = 0,
        )
    }
}
