package team.retum.network.datasource.application

import team.retum.network.api.ApplicationApi
import team.retum.network.model.request.application.ApplyCompanyRequest
import team.retum.network.model.response.application.FetchAppliedCompaniesResponse
import team.retum.network.model.response.application.FetchEmploymentCountResponse
import team.retum.network.model.response.application.FetchEmploymentStatusResponse
import team.retum.network.model.response.application.FetchRejectionReasonResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class RemoteApplicationDataSourceImpl @Inject constructor(
    private val applicationApi: ApplicationApi,
) : RemoteApplicationDataSource {
    override suspend fun fetchAppliedCompanies(): FetchAppliedCompaniesResponse {
        return RequestHandler<FetchAppliedCompaniesResponse>().request {
            applicationApi.fetchAppliedCompanies()
        }
    }

    override suspend fun fetchEmploymentCount(): FetchEmploymentCountResponse {
        return RequestHandler<FetchEmploymentCountResponse>().request {
            applicationApi.fetchEmploymentCount()
        }
    }

    override suspend fun fetchRejectionReason(applicationId: Long): FetchRejectionReasonResponse {
        return RequestHandler<FetchRejectionReasonResponse>().request {
            applicationApi.fetchRejectionReason(applicationId = applicationId)
        }
    }

    override suspend fun applyCompany(
        recruitmentId: Long,
        applyCompanyRequest: ApplyCompanyRequest,
    ) {
        RequestHandler<Unit>().request {
            applicationApi.applyCompany(
                recruitmentId = recruitmentId,
                applyCompanyRequest = applyCompanyRequest,
            )
        }
    }

    override suspend fun reApplyCompany(
        applicationId: Long,
        applyCompanyRequest: ApplyCompanyRequest,
    ) {
        RequestHandler<Unit>().request {
            applicationApi.reApplyCompany(
                applicationId = applicationId,
                applyCompanyRequest = applyCompanyRequest,
            )
        }
    }

    override suspend fun fetchEmploymentStatus(): FetchEmploymentStatusResponse {
        return RequestHandler<FetchEmploymentStatusResponse>().request {
            applicationApi.fetchEmploymentStatus()
        }
    }
}
