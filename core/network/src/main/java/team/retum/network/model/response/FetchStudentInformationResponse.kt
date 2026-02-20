package team.retum.network.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import team.retum.common.enums.Department

@JsonClass(generateAdapter = true)
data class FetchStudentInformationResponse(
    @Json(name = "student_name") val studentName: String,
    @Json(name = "student_gcn") val studentGcn: String,
    @Json(name = "department") val department: Department,
    @Json(name = "profile_image_url") val profileImageUrl: String,
)
