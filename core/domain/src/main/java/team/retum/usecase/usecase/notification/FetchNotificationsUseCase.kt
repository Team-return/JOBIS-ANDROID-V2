package team.retum.usecase.usecase.notification

import team.retum.data.repository.notification.NotificationRepository
import team.retum.usecase.entity.notification.toNotificationsEntity
import javax.inject.Inject

class FetchNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(
        isNew: Boolean?,
    ) = runCatching {
        notificationRepository.fetchNotifications(isNew = isNew).toNotificationsEntity()
    }
}
