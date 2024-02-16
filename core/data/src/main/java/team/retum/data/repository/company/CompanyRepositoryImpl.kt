package team.retum.data.repository.company

import team.retum.network.datasource.company.CompanyDataSource
import team.retum.network.model.response.CompaniesResponse
import team.retum.network.model.response.CompanyCountResponse
import javax.inject.Inject

class CompanyRepositoryImpl @Inject constructor(
    private val companyDataSource: CompanyDataSource,
) : CompanyRepository {
    override suspend fun fetchCompanies(page: Int, name: String?): CompaniesResponse =
        companyDataSource.fetchCompanies(page = page, name = name)

    override suspend fun fetchPageCount(name: String?): CompanyCountResponse =
        companyDataSource.fetchPageCount(name = name)

}