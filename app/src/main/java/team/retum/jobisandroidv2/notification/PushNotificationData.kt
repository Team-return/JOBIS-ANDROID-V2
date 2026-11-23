package team.retum.jobisandroidv2.notification

import android.content.Intent
import android.os.Bundle
import team.retum.common.enums.AlarmType
import team.retum.device.NotificationData

internal data class PushNotificationData(
    val topic: AlarmType?,
    val detailId: Long?,
) {
    companion object {
        private const val STATE_TOPIC = "state_topic"
        private const val STATE_DETAIL_ID = "state_detail_id"
        private const val FCM_MESSAGE_ID = "google.message_id"

        fun fromIntent(intent: Intent?): PushNotificationData? {
            val extras = intent?.extras ?: return null
            if (!intent.isPushNotification()) {
                return null
            }

            return PushNotificationData(
                topic = extras.getString(NotificationData.TOPIC)?.let(::alarmTypeOfOrNull),
                detailId = extras.getString(NotificationData.DETAIL_ID)?.toLongOrNull(),
            )
        }

        fun fromSavedState(savedInstanceState: Bundle): PushNotificationData =
            PushNotificationData(
                topic = savedInstanceState.getString(STATE_TOPIC)?.let(::alarmTypeOfOrNull),
                detailId = savedInstanceState
                    .takeIf { it.containsKey(STATE_DETAIL_ID) }
                    ?.getLong(STATE_DETAIL_ID),
            )

        private fun alarmTypeOfOrNull(value: String): AlarmType? =
            AlarmType.entries.firstOrNull { it.name.equals(value, ignoreCase = true) }

        private fun Intent.isPushNotification(): Boolean =
            getBooleanExtra(NotificationData.IS_PUSH_NOTIFICATION, false) ||
                getStringExtra(NotificationData.IS_PUSH_NOTIFICATION).toBoolean() ||
                hasExtra(FCM_MESSAGE_ID)
    }

    fun saveState(outState: Bundle) {
        outState.putString(STATE_TOPIC, topic?.name)
        detailId?.let { outState.putLong(STATE_DETAIL_ID, it) }
    }
}
