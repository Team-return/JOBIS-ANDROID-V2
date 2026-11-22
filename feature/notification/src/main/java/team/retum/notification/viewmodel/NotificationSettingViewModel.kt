package team.retum.notification.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import team.retum.common.base.BaseViewModel
import team.retum.common.enums.NotificationTopic
import team.retum.usecase.usecase.notification.FetchNotificationSettingsStatusesUseCase
import team.retum.usecase.usecase.notification.SettingAllNotificationUseCase
import team.retum.usecase.usecase.notification.SettingNotificationUseCase
import team.retum.usecase.usecase.user.GetDeviceTokenUseCase
import javax.inject.Inject

@HiltViewModel
internal class NotificationSettingViewModel @Inject constructor(
    private val getDeviceTokenUseCase: GetDeviceTokenUseCase,
    private val fetchNotificationSettingsStatusesUseCase: FetchNotificationSettingsStatusesUseCase,
    private val settingAllNotificationUseCase: SettingAllNotificationUseCase,
    private val settingNotificationUseCase: SettingNotificationUseCase,
) : BaseViewModel<NotificationSettingState, NotificationSettingSideEffect>(NotificationSettingState.getDefaultState()) {

    private lateinit var deviceToken: String
    private val notificationSettingMutex = Mutex()

    init {
        getDeviceToken()
        fetchNotificationSettingsStatuses()
    }

    private fun getDeviceToken() {
        viewModelScope.launch(Dispatchers.IO) {
            getDeviceTokenUseCase().onSuccess {
                deviceToken = it
            }
        }
    }

    private fun fetchNotificationSettingsStatuses() {
        viewModelScope.launch(Dispatchers.IO) {
            notificationSettingMutex.withLock {
                refreshNotificationSettingsStatuses()
            }
        }
    }

    internal fun setAllNotification(isSubscribed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            notificationSettingMutex.withLock {
                settingAllNotificationUseCase()
                    .onSuccess {
                        val isAllSubscribed = refreshNotificationSettingsStatuses()
                        if (isSubscribed && isAllSubscribed == false) {
                            settingAllNotificationUseCase()
                                .onSuccess { refreshNotificationSettingsStatuses() }
                                .onFailure {
                                    postSideEffect(NotificationSettingSideEffect.ChangeNotificationFailure)
                                }
                        }
                    }
                    .onFailure {
                        postSideEffect(NotificationSettingSideEffect.ChangeNotificationFailure)
                    }
            }
        }
    }

    internal fun setNotificationTopic(topic: NotificationTopic) {
        viewModelScope.launch(Dispatchers.IO) {
            notificationSettingMutex.withLock {
                settingNotificationUseCase(
                    topic = topic,
                )
                    .onSuccess { refreshNotificationSettingsStatuses() }
                    .onFailure {
                        postSideEffect(NotificationSettingSideEffect.ChangeNotificationFailure)
                    }
            }
        }
    }

    private suspend fun refreshNotificationSettingsStatuses(): Boolean? =
        fetchNotificationSettingsStatusesUseCase()
            .onSuccess { topics ->
                topics.topics.forEach {
                    changeSubscribeState(
                        topic = it.topic,
                        isSubscribed = it.subscribed,
                    )
                }
            }
            .onFailure {
                postSideEffect(NotificationSettingSideEffect.CanNotCurrentNotificationStatus)
            }
            .getOrNull()
            ?.let { state.value.isAllSubscribe }

    private fun changeSubscribeState(
        topic: NotificationTopic,
        isSubscribed: Boolean,
    ) {
        with(state.value) {
            val subscribeState = when (topic) {
                NotificationTopic.NOTICE -> copy(isNoticeSubscribe = isSubscribed)
                NotificationTopic.APPLICATION -> copy(isApplicationSubscribe = isSubscribed)
                NotificationTopic.RECRUITMENT -> copy(isRecruitmentSubscribe = isSubscribed)
                NotificationTopic.WINTER_INTERN -> copy(isWinterInternSubscribe = isSubscribed)
                NotificationTopic.INTERVIEW -> copy(isInterviewSubscribe = isSubscribed)
            }
            setState {
                subscribeState.copy(
                    isAllSubscribe =
                    subscribeState.isNoticeSubscribe &&
                        subscribeState.isApplicationSubscribe &&
                        subscribeState.isRecruitmentSubscribe &&
                        subscribeState.isWinterInternSubscribe &&
                        subscribeState.isInterviewSubscribe,
                )
            }
        }
    }
}

internal data class NotificationSettingState(
    val isSwitchDisabled: Boolean,
    val isAllSubscribe: Boolean,
    val isNoticeSubscribe: Boolean,
    val isApplicationSubscribe: Boolean,
    val isRecruitmentSubscribe: Boolean,
    val isWinterInternSubscribe: Boolean,
    val isInterviewSubscribe: Boolean,
) {
    companion object {
        fun getDefaultState() = NotificationSettingState(
            isSwitchDisabled = false,
            isAllSubscribe = true,
            isNoticeSubscribe = true,
            isApplicationSubscribe = true,
            isRecruitmentSubscribe = true,
            isWinterInternSubscribe = true,
            isInterviewSubscribe = true,
        )
    }
}

internal sealed interface NotificationSettingSideEffect {
    data object CanNotCurrentNotificationStatus : NotificationSettingSideEffect
    data object ChangeNotificationFailure : NotificationSettingSideEffect
}
