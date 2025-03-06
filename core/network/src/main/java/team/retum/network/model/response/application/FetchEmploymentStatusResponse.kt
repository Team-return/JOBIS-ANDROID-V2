package team.retum.network.model.response.application

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchEmploymentStatusResponse(
    @Json(name = "classes") val classes: List<ClassEmploymentStatusResponse>,
) {
    @JsonClass(generateAdapter = true)
    data class ClassEmploymentStatusResponse(
        @Json(name = "class_id") val classId: Int,
        @Json(name = "employment_rate_response_list") val employmentRateList: List<GetEmploymentRateList>,
        @Json(name = "total_students") val totalStudents: Int,
        @Json(name = "passed_students") val passedStudents: Int,
    ) {
        @JsonClass(generateAdapter = true)
        data class GetEmploymentRateList(
            @Json(name = "id") val id: Int,
            @Json(name = "company_name") val companyName: String,
            @Json(name = "logo_url") val logoUrl: String,
        )
    }
}
