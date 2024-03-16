package team.retum.notification.model

import team.retum.common.enums.AlarmType

data class NotificationDetailData(
    val isNew: Boolean,
    val notificationId: Long,
    val detailId: Long,
    val topic: AlarmType,
)
