package team.retum.network.model.response

import com.google.gson.annotations.SerializedName

data class FetchCodesResponse(
    @SerializedName("codes") val codes: List<CodeResponse>,
) {
    data class CodeResponse(
        @SerializedName("code") val code: Long,
        @SerializedName("keyword") val keyword: String,
    )
}
