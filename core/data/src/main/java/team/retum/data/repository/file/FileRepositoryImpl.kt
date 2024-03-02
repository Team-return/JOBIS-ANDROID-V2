package team.retum.data.repository.file

import team.retum.network.datasource.file.RemoteFileDataSource
import team.retum.network.model.request.file.CreatePresignedUrlRequest
import team.retum.network.model.response.file.CreatePresignedUrlResponse
import java.io.File
import java.nio.file.Files
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val remoteFileDataSource: RemoteFileDataSource,
) : FileRepository {
    override suspend fun createPresignedUrl(createPresignedUrlRequest: CreatePresignedUrlRequest): CreatePresignedUrlResponse {
        return remoteFileDataSource.createPresignedUrl(createPresignedUrlRequest = createPresignedUrlRequest)
    }

    override suspend fun uploadFile(
        presignedUrl: String,
        file: File,
    ) {
        remoteFileDataSource.uploadFile(
            presignedUrl = presignedUrl,
            file = file,
        )
    }
}
