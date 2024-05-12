package team.retum.network.model.response.file

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreatePresignedUrlResponse(
    @Json(name = "urls") val urls: List<UrlResponse>
) {
    data class UrlResponse(
        @Json(name = "file_path") val filePath: String,
        @Json(name = "pre_signed_url") val preSignedUrl: String,
    )
}
