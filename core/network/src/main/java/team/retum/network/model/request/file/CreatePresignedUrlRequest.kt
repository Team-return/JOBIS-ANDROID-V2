package team.retum.network.model.request.file

import com.google.gson.annotations.SerializedName
import team.retum.common.enums.FileType

data class CreatePresignedUrlRequest(
    @SerializedName("files") val files: List<FileRequest>,
) {
    data class FileRequest(
        @SerializedName("type") val type: FileType,
        @SerializedName("file_name") val fileName: String,
    )
}
