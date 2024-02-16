package team.retum.network.model.response

import com.google.gson.annotations.SerializedName

data class CompanyCountResponse(
    @SerializedName("total_page_count") val totalPageCount: Long,
)
