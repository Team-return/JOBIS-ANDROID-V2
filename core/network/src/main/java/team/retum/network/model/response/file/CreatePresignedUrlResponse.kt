package team.retum.network.model.response.file

import com.google.gson.annotations.SerializedName
import retrofit2.http.Path

data class CreatePresignedUrlResponse(
    @SerializedName("urls") val urls: List<UrlResponse>
) {
    data class UrlResponse(
        @SerializedName("file_path") val filePath: String,
        @SerializedName("pre_signed_url") val preSignedUrl: String,
    )
}
