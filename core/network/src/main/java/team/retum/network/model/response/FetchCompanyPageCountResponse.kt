package team.retum.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchCompanyPageCountResponse(
    @Json(name = "total_page_count") val totalPageCount: Long,
)
