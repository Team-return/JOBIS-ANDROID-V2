package team.retum.network.datasource.application

import team.retum.network.model.request.application.ApplyCompanyRequest
import team.retum.network.model.response.application.FetchAppliedCompaniesResponse
import team.retum.network.model.response.application.FetchEmploymentCountResponse
import team.retum.network.model.response.application.FetchEmploymentStatusResponse
import team.retum.network.model.response.application.FetchRejectionReasonResponse

interface RemoteApplicationDataSource {
    suspend fun fetchAppliedCompanies(): FetchAppliedCompaniesResponse

    suspend fun fetchEmploymentCount(year: Int): FetchEmploymentCountResponse

    suspend fun fetchRejectionReason(applicationId: Long): FetchRejectionReasonResponse

    suspend fun applyCompany(
        recruitmentId: Long,
        applyCompanyRequest: ApplyCompanyRequest,
    )

    suspend fun reApplyCompany(
        applicationId: Long,
        applyCompanyRequest: ApplyCompanyRequest,
    )

    suspend fun fetchEmploymentStatus(year: Int): FetchEmploymentStatusResponse
}
