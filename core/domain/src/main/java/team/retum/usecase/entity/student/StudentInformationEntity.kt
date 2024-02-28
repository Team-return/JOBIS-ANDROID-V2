package team.retum.usecase.entity.student

import team.retum.common.enums.Department
import team.retum.network.model.response.FetchStudentInformationResponse

data class StudentInformationEntity(
    val studentName: String,
    val studentGcn: String,
    val department: Department,
    val profileImageUrl: String,
)

internal fun FetchStudentInformationResponse.toEntity() = StudentInformationEntity(
    studentName = this.studentName,
    studentGcn = this.studentGcn,
    department = this.department,
    profileImageUrl = this.profileImageUrl,
)
