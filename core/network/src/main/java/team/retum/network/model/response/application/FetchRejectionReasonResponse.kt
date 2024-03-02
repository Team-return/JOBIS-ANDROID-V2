package team.retum.network.model.response.application

import com.google.gson.annotations.SerializedName

data class FetchRejectionReasonResponse(
    @SerializedName("rejection_reason") val rejectionReason: String,
)
