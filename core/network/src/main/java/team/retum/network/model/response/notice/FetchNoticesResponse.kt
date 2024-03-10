package team.retum.network.model.response.notice

import com.google.gson.annotations.SerializedName

data class FetchNoticesResponse (
    @SerializedName("notices") val notices: List<NoticeResponse>
) {
    data class NoticeResponse(
        @SerializedName("notice_id") val noticeId: Long,
        @SerializedName("title") val title: String,
        @SerializedName("createAt") val createAt: String,
    )
}
