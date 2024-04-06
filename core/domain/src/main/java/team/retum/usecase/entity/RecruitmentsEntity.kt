package team.retum.usecase.entity

import team.retum.common.utils.ResourceKeys
import team.retum.network.model.response.FetchRecruitmentsResponse

enum class MilitarySupport {
    TRUE,
    FALSE,
    LOADING,
}

data class RecruitmentsEntity(
    val recruitments: List<RecruitmentEntity>,
) {
    data class RecruitmentEntity(
        val id: Long,
        val companyName: String,
        val companyProfileUrl: String,
        val trainPay: Long,
        val militarySupport: MilitarySupport,
        val hiringJobs: String,
        val bookmarked: Boolean,
    ) {
        companion object {
            fun getDefaultEntity() = RecruitmentEntity(
                id = 0,
                companyName = "",
                companyProfileUrl = "",
                trainPay = 0,
                militarySupport = MilitarySupport.LOADING,
                hiringJobs = "",
                bookmarked = false,
            )
        }
    }
}

internal fun FetchRecruitmentsResponse.toRecruitmentsEntity() = RecruitmentsEntity(
    recruitments = this.recruitments.map { it.toEntity() },
)

private fun FetchRecruitmentsResponse.RecruitmentResponse.toEntity() =
    RecruitmentsEntity.RecruitmentEntity(
        id = this.id,
        companyName = this.companyName,
        companyProfileUrl = ResourceKeys.IMAGE_URL + this.companyProfileUrl,
        trainPay = this.trainPay,
        militarySupport = when (this.militarySupport) {
            true -> MilitarySupport.TRUE
            false -> MilitarySupport.FALSE
        },
        hiringJobs = this.hiringJobs,
        bookmarked = this.bookmarked,
    )
