package team.retum.network.model.response.notice

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchNoticesCountResponse(
    @Json(name = "total_page_count") val totalPageCount: Long,
)
