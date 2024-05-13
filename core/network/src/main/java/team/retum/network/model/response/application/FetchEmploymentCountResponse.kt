package team.retum.network.model.response.application

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchEmploymentCountResponse(
    @Json(name = "total_student_count") val totalStudentCount: Long,
    @Json(name = "passed_count") val passCount: Long,
    @Json(name = "approved_count") val approvedCount: Long,
)
