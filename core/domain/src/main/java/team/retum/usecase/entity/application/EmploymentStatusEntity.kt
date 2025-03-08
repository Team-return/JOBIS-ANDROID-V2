package team.retum.usecase.entity.application

import team.retum.common.utils.ResourceKeys
import team.retum.network.model.response.application.FetchEmploymentStatusResponse

data class EmploymentStatusEntity(
    val classes: List<ClassEmploymentStatusEntity>,
) {
    data class ClassEmploymentStatusEntity(
        val classId: Int,
        val employmentRateList: List<GetEmploymentRateList>,
        val totalStudents: Int,
        val passedStudents: Int,
    ) {
        data class GetEmploymentRateList(
            val id: Int,
            val companyName: String,
            val logoUrl: String,
        )
    }
}

internal fun FetchEmploymentStatusResponse.toEntity() = EmploymentStatusEntity(
    classes = this.classes.map { it.toEntity() },
)

private fun FetchEmploymentStatusResponse.ClassEmploymentStatusResponse.toEntity() =
    EmploymentStatusEntity.ClassEmploymentStatusEntity(
        classId = this.classId,
        employmentRateList = this.employmentRateList.map { it.toEntity() },
        totalStudents = this.totalStudents,
        passedStudents = this.passedStudents,
    )

private fun FetchEmploymentStatusResponse.ClassEmploymentStatusResponse.FetchEmploymentRate.toEntity() =
    EmploymentStatusEntity.ClassEmploymentStatusEntity.GetEmploymentRateList(
        id = this.id,
        companyName = this.companyName,
        logoUrl = ResourceKeys.IMAGE_URL + this.logoUrl,
    )
