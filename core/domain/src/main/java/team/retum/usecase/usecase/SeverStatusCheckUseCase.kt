package team.retum.usecase.usecase

import team.retum.data.repository.ServerStatusCheckRepository
import javax.inject.Inject

class ServerStatusCheckUseCase @Inject constructor(
    private val serverStatusCheckRepository: ServerStatusCheckRepository,
) {
    suspend operator fun invoke() = runCatching {
        serverStatusCheckRepository.serverStatusCheck()
    }
}
