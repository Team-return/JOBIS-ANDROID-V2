package team.retum.usecase.usecase.notification

import team.retum.common.enums.NotificationTopic
import team.retum.data.repository.notification.NotificationRepository
import javax.inject.Inject

class SettingNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(
        topic: NotificationTopic,
    ) = runCatching {
        notificationRepository.settingNotification(topic = topic)
    }
}
