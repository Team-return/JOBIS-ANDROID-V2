package team.retum.usecase.usecase.file

import team.retum.common.enums.FileType
import team.retum.data.repository.file.FileRepository
import team.retum.network.model.request.file.CreatePresignedUrlRequest
import team.retum.usecase.entity.file.toEntity
import javax.inject.Inject

class CreatePresignedUrlUseCase @Inject constructor(
    private val fileRepository: FileRepository,
) {
    suspend operator fun invoke(files: List<String>) = runCatching {
        fileRepository.createPresignedUrl(
            createPresignedUrlRequest = CreatePresignedUrlRequest(
                files = files.map {
                    CreatePresignedUrlRequest.FileRequest(
                        type = FileType.EXTENSION_FILE,
                        fileName = it,
                    )
                },
            ),
        ).toEntity()
    }
}
