package team.retum.usecase.usecase.student

import team.retum.data.repository.company.CompanyRepository
import team.retum.network.model.response.RecentResponse
import javax.inject.Inject

class FetchRecentCompaniesUseCase @Inject constructor(
    private val companyRepository: CompanyRepository,
) {
    suspend operator fun invoke(): RecentResponse {
        return companyRepository.fetchRecentCompanies()
    }
}
