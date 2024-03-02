package team.retum.network.datasource.file

import team.retum.network.model.request.file.CreatePresignedUrlRequest
import team.retum.network.model.response.file.CreatePresignedUrlResponse
import java.io.File

interface RemoteFileDataSource {
    suspend fun createPresignedUrl(createPresignedUrlRequest: CreatePresignedUrlRequest): CreatePresignedUrlResponse
    suspend fun uploadFile(
        presignedUrl: String,
        file: File,
    )
}
