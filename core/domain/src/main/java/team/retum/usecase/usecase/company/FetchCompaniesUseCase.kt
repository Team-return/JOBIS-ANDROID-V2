package team.retum.usecase.usecase.company

import team.retum.common.enums.CompanySortType
import team.retum.data.repository.company.CompanyRepository
import team.retum.usecase.entity.toCompaniesEntity
import javax.inject.Inject

class FetchCompaniesUseCase @Inject constructor(
    private val companyRepository: CompanyRepository,
) {
    suspend operator fun invoke(
        page: Int,
        name: String?,
        sortType: CompanySortType? = null,
    ) = runCatching {
        companyRepository.fetchCompanies(
            page = page,
            name = name,
            sortType = sortType?.name,
        ).toCompaniesEntity()
    }
}
