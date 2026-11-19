package team.retum.usecase.usecase.application

import team.retum.data.repository.application.ApplicationRepository
import javax.inject.Inject

class CancelApplicationUseCase @Inject constructor(
    private val applicationRepository: ApplicationRepository,
) {
    suspend operator fun invoke(applicationId: Long) = runCatching {
        applicationRepository.cancelApplication(applicationId = applicationId)
    }
}
