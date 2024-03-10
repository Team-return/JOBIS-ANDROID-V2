package team.retum.network.model.request.bug

import com.google.gson.annotations.SerializedName
import team.retum.common.enums.DevelopmentArea

data class ReportBugRequest(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("development_area") val developmentArea: DevelopmentArea,
    @SerializedName("attachment_url") val attachmentUrls: List<String>,
)
