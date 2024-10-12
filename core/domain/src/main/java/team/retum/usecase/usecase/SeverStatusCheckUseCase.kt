package team.retum.usecase.usecase

import team.retum.data.repository.SeverStatusCheckRepository
import javax.inject.Inject

class SeverStatusCheckUseCase @Inject constructor(
    private val severStatusCheckRepository: SeverStatusCheckRepository,
) {
    suspend operator fun invoke() = kotlin.runCatching {
        severStatusCheckRepository.severStatusCheck()
    }
}
