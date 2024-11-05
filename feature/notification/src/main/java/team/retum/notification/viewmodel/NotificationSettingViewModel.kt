package team.retum.notification.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        }
    }

    internal fun setAllNotification(isSubscribed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingAllNotificationUseCase()
                .onFailure {
                    postSideEffect(NotificationSettingSideEffect.ChangeNotificationFailure)
                }
        }
        setState {
            state.value.copy(
                isAllSubscribe = isSubscribed,
                isNoticeSubscribe = isSubscribed,
                isRecruitmentSubscribe = isSubscribed,
                isApplicationSubscribe = isSubscribed,
                isWinterInternSubscribe = isSubscribed,
            )
        }
    }

    internal fun setNotificationTopic(
        topic: NotificationTopic,
        isSubscribed: Boolean,
    ) {
        changeSubscribeState(
            topic = topic,
            isSubscribed = isSubscribed,
        )

        viewModelScope.launch(Dispatchers.IO) {
            settingNotificationUseCase(
                topic = topic,
            ).onFailure {
                postSideEffect(NotificationSettingSideEffect.ChangeNotificationFailure)
            }
        }
    }

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
            }
            setState { subscribeState }
        }
        with(state.value) {
            val isAllSubscribe =
                isNoticeSubscribe && isApplicationSubscribe && isRecruitmentSubscribe && isWinterInternSubscribe
            setState { copy(isAllSubscribe = isAllSubscribe) }
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
) {
    companion object {
        fun getDefaultState() = NotificationSettingState(
            isSwitchDisabled = false,
            isAllSubscribe = true,
            isNoticeSubscribe = true,
            isApplicationSubscribe = true,
            isRecruitmentSubscribe = true,
            isWinterInternSubscribe = true,
        )
    }
}

internal sealed interface NotificationSettingSideEffect {
    data object CanNotCurrentNotificationStatus : NotificationSettingSideEffect
    data object ChangeNotificationFailure : NotificationSettingSideEffect
}
