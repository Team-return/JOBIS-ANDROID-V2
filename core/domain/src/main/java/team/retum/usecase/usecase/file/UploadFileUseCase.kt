package team.retum.usecase.usecase.file

import team.retum.data.repository.file.FileRepository
import java.io.File
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(
    private val fileRepository: FileRepository,
) {
    suspend operator fun invoke(
        presignedUrl: String,
        file: File,
    ) = runCatching {
        fileRepository.uploadFile(
            presignedUrl = presignedUrl,
            file = file,
        )
    }
}
