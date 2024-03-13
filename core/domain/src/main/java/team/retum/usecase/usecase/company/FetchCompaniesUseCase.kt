package team.retum.usecase.usecase.company

import team.retum.data.repository.company.CompanyRepository
import team.retum.usecase.entity.toCompaniesEntity
import javax.inject.Inject

class FetchCompaniesUseCase @Inject constructor(
    private val companyRepository: CompanyRepository,
) {
    suspend operator fun invoke(page: Int, name: String?) = runCatching {
        companyRepository.fetchCompanies(page = page, name = name).toCompaniesEntity()
    }
}
