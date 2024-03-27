package team.retum.data.repository.application

import team.retum.network.datasource.application.RemoteApplicationDataSource
import team.retum.network.model.request.application.ApplyCompanyRequest
import team.retum.network.model.response.application.FetchAppliedCompaniesResponse
import team.retum.network.model.response.application.FetchEmploymentCountResponse
import team.retum.network.model.response.application.FetchRejectionReasonResponse
import javax.inject.Inject

class ApplicationRepositoryImpl @Inject constructor(
    private val remoteApplicationDataSource: RemoteApplicationDataSource,
) : ApplicationRepository {
    override suspend fun fetchAppliedCompanies(): FetchAppliedCompaniesResponse {
        return remoteApplicationDataSource.fetchAppliedCompanies()
    }

    override suspend fun fetchEmploymentCount(): FetchEmploymentCountResponse {
        return remoteApplicationDataSource.fetchEmploymentCount()
    }

    override suspend fun fetchRejectionReason(applicationId: Long): FetchRejectionReasonResponse {
        return remoteApplicationDataSource.fetchRejectionReason(applicationId = applicationId)
    }

    override suspend fun applyCompany(
        recruitmentId: Long,
        applyCompanyRequest: ApplyCompanyRequest,
    ) {
        remoteApplicationDataSource.applyCompany(
            recruitmentId = recruitmentId,
            applyCompanyRequest = applyCompanyRequest,
        )
    }

    override suspend fun reApplyCompany(
        applicationId: Long,
        applyCompanyRequest: ApplyCompanyRequest,
    ) {
        remoteApplicationDataSource.reApplyCompany(
            applicationId = applicationId,
            applyCompanyRequest = applyCompanyRequest,
        )
    }
}
