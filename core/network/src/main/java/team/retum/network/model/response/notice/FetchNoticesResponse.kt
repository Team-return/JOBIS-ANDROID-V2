package team.retum.network.model.response.notice

import com.google.gson.annotations.SerializedName

data class FetchNoticesResponse(
    @SerializedName("notices") val notices: List<NoticeResponse>,
) {
    data class NoticeResponse(
        @SerializedName("id") val id: Long,
        @SerializedName("title") val title: String,
        @SerializedName("created_at") val createdAt: String,
    )
}
