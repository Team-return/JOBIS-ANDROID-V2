package team.retum.network.model.response.application

import com.google.gson.annotations.SerializedName

data class FetchEmploymentCountResponse(
    @SerializedName("total_student_count") val totalStudentCount: Long,
    @SerializedName("passed_count") val passCount: Long,
    @SerializedName("approved_count") val approvedCount: Long,
)
