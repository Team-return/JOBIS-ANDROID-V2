package team.retum.usecase.usecase.company

import team.retum.data.repository.company.CompanyRepository
import team.retum.usecase.entity.toCompanyCountEntity
import javax.inject.Inject

class FetchCompanyCountUseCase @Inject constructor(
    private val companyRepository: CompanyRepository,
) {
    suspend operator fun invoke(name: String?) = runCatching {
        companyRepository.fetchPageCount(name = name).toCompanyCountEntity()
    }
}
