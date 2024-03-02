package team.retum.usecase.entity

import team.retum.network.model.response.FetchRecruitmentsResponse

data class RecruitmentsEntity(
    val recruitments: List<RecruitmentEntity>,
) {
    data class RecruitmentEntity(
        val id: Long,
        val companyName: String,
        val companyProfileUrl: String,
        val trainPay: Long,
        val militarySupport: Boolean,
        val hiringJobs: String,
        val bookmarked: Boolean,
    )
}

internal fun FetchRecruitmentsResponse.toRecruitmentsEntity() = RecruitmentsEntity(
    recruitments = this.recruitments.map { it.toEntity() },
)

private fun FetchRecruitmentsResponse.RecruitmentResponse.toEntity() =
    RecruitmentsEntity.RecruitmentEntity(
        id = this.id,
        companyName = this.companyName,
        companyProfileUrl = this.companyProfileUrl,
        trainPay = this.trainPay,
        militarySupport = this.militarySupport,
        hiringJobs = this.hiringJobs,
        bookmarked = this.bookmarked,
    )
