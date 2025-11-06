package team.retum.network.model.request.interests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InterestsToggleRequest(
    @Json(name = "code_ids") val codeIds: List<Long>,
)
