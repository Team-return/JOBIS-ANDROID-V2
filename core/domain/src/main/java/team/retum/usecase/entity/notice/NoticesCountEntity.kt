package team.retum.usecase.entity.notice

import team.retum.network.model.response.notice.FetchNoticesCountResponse

data class NoticesCountEntity(
    val totalPageCount: Long,
)

internal fun FetchNoticesCountResponse.toEntity() = NoticesCountEntity(
    totalPageCount = totalPageCount,
)
