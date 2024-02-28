package team.retum.network.model.response

import com.google.gson.annotations.SerializedName
import team.retum.common.enums.Department

data class FetchStudentInformationResponse(
    @SerializedName("student_name") val studentName: String,
    @SerializedName("student_gcn") val studentGcn: String,
    @SerializedName("department") val department: Department,
    @SerializedName("profile_image_url") val profileImageUrl: String,
)
