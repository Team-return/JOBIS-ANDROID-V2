package team.retum.network.api

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url
import team.retum.network.di.RequestUrls
import team.retum.network.model.request.file.CreatePresignedUrlRequest
import team.retum.network.model.response.file.CreatePresignedUrlResponse

interface FileApi {
    @POST(RequestUrls.Files.presignedUrl)
    suspend fun createPresignedUrl(
        @Body createPresignedUrlRequest: CreatePresignedUrlRequest,
    ): CreatePresignedUrlResponse

    @PUT
    suspend fun uploadFile(
        @Url presignedUrl: String,
        @Body file: RequestBody,
    )
}
