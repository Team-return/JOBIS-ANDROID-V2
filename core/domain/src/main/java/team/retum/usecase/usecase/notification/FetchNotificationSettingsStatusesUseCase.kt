package team.retum.usecase.usecase.notification

import team.retum.data.repository.notification.NotificationRepository
import team.retum.usecase.entity.notification.toTopicsEntity
import javax.inject.Inject

class FetchNotificationSettingsStatusesUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke() =
        runCatching {
            notificationRepository.fetchNotificationSettingStatuses().toTopicsEntity()
        }
}
