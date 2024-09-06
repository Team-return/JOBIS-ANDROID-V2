package team.retum.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchCodesResponse(
    @Json(name = "codes") val codes: List<CodeResponse>,
) {
    @JsonClass(generateAdapter = true)
    data class CodeResponse(
        @Json(name = "code") val code: Long,
        @Json(name = "keyword") val keyword: String,
    )
}
