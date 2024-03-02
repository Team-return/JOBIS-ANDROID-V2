package team.retum.usecase.usecase.application

import team.retum.data.repository.application.ApplicationRepository
import team.retum.usecase.entity.application.toEntity
import javax.inject.Inject

class FetchAppliedCompaniesUseCase @Inject constructor(
    private val applicationRepository: ApplicationRepository,
) {
    suspend operator fun invoke() = runCatching {
        applicationRepository.fetchAppliedCompanies().toEntity()
    }
}
