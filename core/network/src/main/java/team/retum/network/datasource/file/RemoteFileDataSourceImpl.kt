package team.retum.network.datasource.file

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import team.retum.common.utils.RequestType
import team.retum.network.api.FileApi
import team.retum.network.model.request.file.CreatePresignedUrlRequest
import team.retum.network.model.response.file.CreatePresignedUrlResponse
import team.retum.network.util.RequestHandler
import java.io.File
import java.nio.file.Files
import javax.inject.Inject

class RemoteFileDataSourceImpl @Inject constructor(
    private val fileApi: FileApi,
) : RemoteFileDataSource {
    override suspend fun createPresignedUrl(createPresignedUrlRequest: CreatePresignedUrlRequest): CreatePresignedUrlResponse {
        return RequestHandler<CreatePresignedUrlResponse>().request {
            fileApi.createPresignedUrl(createPresignedUrlRequest = createPresignedUrlRequest)
        }
    }

    override suspend fun uploadFile(
        presignedUrl: String,
        file: File,
    ) {
        RequestHandler<Unit>().request {
            fileApi.uploadFile(
                presignedUrl = presignedUrl,
                file = Files.readAllBytes(file.toPath()).toRequestBody(
                    contentType = RequestType.Binary.toMediaTypeOrNull(),
                ),
            )
        }
    }
}
