package team.retum.data.repository.file

import team.retum.network.model.request.file.CreatePresignedUrlRequest
import team.retum.network.model.response.file.CreatePresignedUrlResponse
import java.io.File

interface FileRepository {
    suspend fun createPresignedUrl(createPresignedUrlRequest: CreatePresignedUrlRequest): CreatePresignedUrlResponse
    suspend fun uploadFile(
        presignedUrl: String,
        file: File,
    )
}
