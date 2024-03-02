package team.retum.network.datasource.file

import team.retum.network.model.request.file.CreatePresignedUrlRequest
import team.retum.network.model.response.file.CreatePresignedUrlResponse

interface RemoteFileDataSource {
    suspend fun createPresignedUrl(createPresignedUrlRequest: CreatePresignedUrlRequest): CreatePresignedUrlResponse
}
