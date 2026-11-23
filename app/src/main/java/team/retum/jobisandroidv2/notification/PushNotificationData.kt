package team.retum.jobisandroidv2.notification

import android.content.Intent
import team.retum.common.enums.AlarmType
import team.retum.device.NotificationData

internal data class PushNotificationData(
    val topic: AlarmType?,
    val detailId: Long?,
) {
    companion object {
        fun fromIntent(intent: Intent?): PushNotificationData? {
            val extras = intent?.extras ?: return null
            if (!extras.containsKey(NotificationData.TOPIC) && !extras.containsKey(NotificationData.DETAIL_ID)) {
                return null
            }

            return PushNotificationData(
                topic = extras.getString(NotificationData.TOPIC)?.let(::alarmTypeOfOrNull),
                detailId = extras.getString(NotificationData.DETAIL_ID)?.toLongOrNull(),
            )
        }

        private fun alarmTypeOfOrNull(value: String): AlarmType? =
            AlarmType.entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
    }
}
