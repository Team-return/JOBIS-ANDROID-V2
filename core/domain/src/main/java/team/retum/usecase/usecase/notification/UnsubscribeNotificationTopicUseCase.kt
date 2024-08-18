package team.retum.usecase.usecase.notification

import team.retum.common.enums.NotificationTopic
import team.retum.data.repository.notification.NotificationRepository
import javax.inject.Inject

class UnsubscribeNotificationTopicUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(
        deviceToken: String,
        topic: NotificationTopic,
    ) = runCatching {
        notificationRepository.unsubscribeNotificationTopic(
            deviceToken = deviceToken,
            topic = topic,
        )
    }
}
