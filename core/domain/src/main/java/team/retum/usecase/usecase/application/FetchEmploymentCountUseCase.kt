package team.retum.usecase.usecase.application

import team.retum.data.repository.application.ApplicationRepository
import team.retum.usecase.entity.application.toEntity
import javax.inject.Inject

class FetchEmploymentCountUseCase @Inject constructor(
    private val applicationRepository: ApplicationRepository,
) {
    suspend operator fun invoke(year: Int) = runCatching {
        applicationRepository.fetchEmploymentCount(year = year).toEntity()
    }
}
