package team.retum.usecase.entity

import team.retum.common.enums.HiringProgress
import team.retum.common.utils.ResourceKeys
import team.retum.network.model.response.Areas
import team.retum.network.model.response.FetchRecruitmentDetailsResponse
import team.retum.network.model.response.Job
import team.retum.network.model.response.Tech

data class RecruitmentDetailsEntity(
    val companyId: Long,
    val companyProfileUrl: String,
    val companyName: String,
    val areas: List<AreasEntity>,
    val requiredGrade: Long?,
    val workingHours: String,
    val requiredLicenses: List<String>?,
    val hiringProgress: List<HiringProgress>,
    val trainPay: Long,
    val pay: String?,
    val benefits: String?,
    val military: Boolean,
    val submitDocument: String,
    val startDate: String?,
    val endDate: String?,
    val etc: String?,
    val isApplicable: Boolean,
    val bookmarked: Boolean,
)

data class AreasEntity(
    val id: Long,
    val job: List<JobEntity>,
    val tech: List<TechEntity>,
    val hiring: Long,
    val majorTask: String,
    val preferentialTreatment: String?,
)

data class JobEntity(
    val id: Long,
    val name: String,
)

data class TechEntity(
    val id: Long,
    val name: String,
)

internal fun FetchRecruitmentDetailsResponse.toEntity() = RecruitmentDetailsEntity(
    companyId = this.companyId,
    companyProfileUrl = ResourceKeys.IMAGE_URL + this.companyProfileUrl,
    companyName = this.companyName,
    areas = this.areas.map { it.toEntity() },
    requiredGrade = this.requiredGrade,
    workingHours = this.workingHours,
    requiredLicenses = this.requiredLicenses,
    hiringProgress = this.hiringProgress,
    trainPay = this.trainPay,
    pay = this.pay,
    benefits = this.benefits,
    military = this.military,
    submitDocument = this.submitDocument,
    startDate = this.startDate,
    endDate = this.endDate,
    etc = this.etc,
    isApplicable = this.isApplicable,
    bookmarked = this.bookmarked,
)

private fun Areas.toEntity() = AreasEntity(
    id = this.id,
    job = this.job.map { it.toEntity() },
    tech = this.tech.map { it.toEntity() },
    hiring = this.hiring,
    majorTask = this.majorTask,
    preferentialTreatment = this.preferentialTreatment,
)

private fun Job.toEntity() = JobEntity(
    id = this.id,
    name = this.name,
)

private fun Tech.toEntity() = TechEntity(
    id = this.id,
    name = this.name,
)
