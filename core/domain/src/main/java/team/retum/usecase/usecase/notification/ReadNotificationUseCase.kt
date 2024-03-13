package team.retum.usecase.usecase.notification

import team.retum.data.repository.notification.NotificationRepository
import javax.inject.Inject

class ReadNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(
        notificationId: Long,
    ) = runCatching {
        notificationRepository.readNotification(notificationId = notificationId)
    }
}
