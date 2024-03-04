package team.retum.usecase.usecase.company

import team.retum.data.repository.company.CompanyRepository
import team.retum.usecase.entity.company.toEntity
import javax.inject.Inject

class FetchReviewableCompaniesUseCase @Inject constructor(
    private val companyRepository: CompanyRepository,
) {
    suspend operator fun invoke() = runCatching {
        companyRepository.fetchReviewableCompanies().toEntity()
    }
}
