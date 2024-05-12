package team.retum.network.model.request.file

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import team.retum.common.enums.FileType

@JsonClass(generateAdapter = true)
data class CreatePresignedUrlRequest(
    @Json(name = "files") val files: List<FileRequest>,
) {
    data class FileRequest(
        @Json(name = "type") val type: FileType,
        @Json(name = "file_name") val fileName: String,
    )
}
