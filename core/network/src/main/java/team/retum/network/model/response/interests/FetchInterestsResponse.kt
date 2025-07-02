package team.retum.network.model.response.interests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchInterestsResponse(
    @Json(name = "student_name") val studentName: String,
    @Json(name = "interests") val interests: List<InterestMajor>,
) {
    data class InterestMajor(
        @Json(name = "id") val id: Int,
        @Json(name = "student_id") val studentId: Int,
        @Json(name = "code") val code: Int,
        @Json(name = "keyword") val keyword: String,
    )
}
