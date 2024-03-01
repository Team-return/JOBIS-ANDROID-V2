package team.retum.usecase.usecase.company

import team.retum.data.repository.company.CompanyRepository
import team.retum.usecase.entity.company.toEntity
import javax.inject.Inject

class FetchCompanyDetailsUseCase @Inject constructor(
    private val companyRepository: CompanyRepository,
) {
    suspend operator fun invoke(companyId: Long) = runCatching {
        companyRepository.fetchCompanyDetails(companyId = companyId).toEntity()
    }
}
