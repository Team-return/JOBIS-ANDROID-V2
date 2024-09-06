package team.retum.network.model.response.application

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchRejectionReasonResponse(
    @Json(name = "rejection_reason") val rejectionReason: String,
)
