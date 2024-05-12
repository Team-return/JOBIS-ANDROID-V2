package team.retum.network.model.request.bug

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import team.retum.common.enums.DevelopmentArea

@JsonClass(generateAdapter = true)
data class ReportBugRequest(
    @Json(name = "title") val title: String,
    @Json(name = "content") val content: String,
    @Json(name = "development_area") val developmentArea: DevelopmentArea,
    @Json(name = "attachment_urls") val attachmentUrls: List<String>,
)
