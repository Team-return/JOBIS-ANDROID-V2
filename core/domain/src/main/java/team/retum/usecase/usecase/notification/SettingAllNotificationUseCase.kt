package team.retum.usecase.usecase.notification

import team.retum.data.repository.notification.NotificationRepository
import javax.inject.Inject

class SettingAllNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke() =
        runCatching {
            notificationRepository.settingAllNotification()
        }
}
