package team.retum.network.datasource.file

import team.retum.network.FileApi
import team.retum.network.model.request.file.CreatePresignedUrlRequest
import team.retum.network.model.response.file.CreatePresignedUrlResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class RemoteFileDataSourceImpl @Inject constructor(
    private val fileApi: FileApi
): RemoteFileDataSource {
    override suspend fun createPresignedUrl(createPresignedUrlRequest: CreatePresignedUrlRequest): CreatePresignedUrlResponse {
        return RequestHandler<CreatePresignedUrlResponse>().request {
            fileApi.createPresignedUrl(createPresignedUrlRequest = createPresignedUrlRequest)
        }
    }
}
