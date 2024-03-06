package team.retum.usecase.entity.file

import team.retum.network.model.response.file.CreatePresignedUrlResponse

data class PresignedUrlEntity(
    val urls: List<UrlEntity>,
) {
    data class UrlEntity(
        val filePath: String,
        val preSignedUrl: String,
    )
}

internal fun CreatePresignedUrlResponse.toEntity() = PresignedUrlEntity(
    urls = this.urls.map { it.toEntity() },
)

private fun CreatePresignedUrlResponse.UrlResponse.toEntity() = PresignedUrlEntity.UrlEntity(
    filePath = this.filePath,
    preSignedUrl = this.preSignedUrl,
)
