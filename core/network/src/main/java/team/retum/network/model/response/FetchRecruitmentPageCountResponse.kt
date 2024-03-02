package team.retum.network.model.response

import com.google.gson.annotations.SerializedName

data class FetchRecruitmentPageCountResponse(
    @SerializedName("total_page_count") val totalPageCount: Long,
)
