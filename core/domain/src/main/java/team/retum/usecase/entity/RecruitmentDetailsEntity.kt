package team.retum.usecase.entity

import team.retum.common.enums.HiringProgress
import team.retum.network.model.response.Areas
import team.retum.network.model.response.FetchRecruitmentDetailsResponse

data class RecruitmentDetailsEntity(
    val companyProfileUrl: String,
    val companyName: String,
    val areas: List<AreasEntity>,
    val requiredGrade: Long?,
    val startTime: String,
    val endTime: String,
    val requiredLicenses: List<String>?,
    val hiringProgress: List<HiringProgress>,
    val trainPay: Long,
    val pay: String?,
    val benefits: String?,
    val military: Boolean,
    val submitDocument: String,
    val startDate: String,
    val endDate: String,
    val etc: String?,
    val isApplicable: Boolean,
    val bookmarked: Boolean,
)

data class AreasEntity(
    val id: Long,
    val job: List<String>,
    val tech: List<String>,
    val hiring: Long,
    val majorTask: String,
    val preferentialTreatment: String?,
)

internal fun FetchRecruitmentDetailsResponse.toRecruitmentDetailsEntity() =
    RecruitmentDetailsEntity(
        companyProfileUrl = this.companyProfileUrl,
        companyName = this.companyName,
        areas = this.areas.map { it.toEntity() },
        requiredGrade = this.requiredGrade,
        startTime = this.startTime,
        endTime = this.endTime,
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
    job = this.job,
    tech = this.tech,
    hiring = this.hiring,
    majorTask = this.majorTask,
    preferentialTreatment = this.preferentialTreatment,
)
