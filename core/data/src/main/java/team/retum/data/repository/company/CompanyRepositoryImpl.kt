package team.retum.data.repository.company

import team.retum.network.datasource.company.CompanyDataSource
import team.retum.network.model.response.FetchCompaniesResponse
import team.retum.network.model.response.FetchCompanyPageCountResponse
import javax.inject.Inject

class CompanyRepositoryImpl @Inject constructor(
    private val companyDataSource: CompanyDataSource,
) : CompanyRepository {
    override suspend fun fetchCompanies(page: Int, name: String?): FetchCompaniesResponse =
        companyDataSource.fetchCompanies(page = page, name = name)

    override suspend fun fetchPageCount(name: String?): FetchCompanyPageCountResponse =
        companyDataSource.fetchPageCount(name = name)

}