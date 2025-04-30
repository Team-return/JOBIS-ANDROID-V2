package team.retum.network.model.response.interests

import com.squareup.moshi.Json

data class FetchInterestsResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "student_id") val studentId: Int,
    @Json(name = "code") val code: Int,
    @Json(name = "keyword") val keyword: String,
)
