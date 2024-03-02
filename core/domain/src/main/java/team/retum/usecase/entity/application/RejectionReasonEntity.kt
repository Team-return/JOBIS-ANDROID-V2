package team.retum.usecase.entity.application

import team.retum.network.model.response.application.FetchRejectionReasonResponse

data class RejectionReasonEntity(
    val rejectionReason: String,
)

internal fun FetchRejectionReasonResponse.toEntity() = RejectionReasonEntity(
    rejectionReason = this.rejectionReason,
)
