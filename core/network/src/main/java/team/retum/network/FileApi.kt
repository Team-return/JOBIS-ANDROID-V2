package team.retum.network

import retrofit2.http.Body
import retrofit2.http.POST
import team.retum.network.di.RequestUrls
import team.retum.network.model.request.file.CreatePresignedUrlRequest
import team.retum.network.model.response.file.CreatePresignedUrlResponse

interface FileApi {
    @POST(RequestUrls.Files.presignedUrl)
    suspend fun createPresignedUrl(
        @Body createPresignedUrlRequest: CreatePresignedUrlRequest,
    ): CreatePresignedUrlResponse
}
