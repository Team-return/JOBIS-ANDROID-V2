package team.retum.usecase.entity.notification

import team.retum.common.enums.NotificationTopic
import team.retum.network.model.response.notification.FetchNotificationSettingStatusesResponse

data class TopicsEntity(
    val topics: List<TopicEntity>,
) {
    data class TopicEntity(
        val topic: NotificationTopic,
        val subscribed: Boolean,
    )
}

internal fun FetchNotificationSettingStatusesResponse.toTopicsEntity() = TopicsEntity(
    topics = this.topics.map { it.toEntity() },
)

private fun FetchNotificationSettingStatusesResponse.NotificationSettingStatusResponse.toEntity() =
    TopicsEntity.TopicEntity(
        topic = this.topic,
        subscribed = this.subscribed,
    )
