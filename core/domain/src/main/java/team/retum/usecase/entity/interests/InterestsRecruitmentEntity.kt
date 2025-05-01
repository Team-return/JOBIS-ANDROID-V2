package team.retum.usecase.entity.interests

import androidx.compose.runtime.Immutable
import team.retum.network.model.response.interests.FetchInterestsRecruitmentsResponse

@Immutable
data class InterestsRecruitmentsEntity(
    val recruitments : List<InterestsRecruitmentEntity>
) {
    data class InterestsRecruitmentEntity(
        val id : Int,
        val companyId : String,
        val companyProfileUrl : String,
        val trainPay : Int,
        val militarySupport : Boolean,
        val hiringJobs : String,
        val bookmarked : Boolean,
    )
}

internal fun FetchInterestsRecruitmentsResponse.toEntity() = InterestsRecruitmentsEntity(
    recruitments = this.recruitments.map { it.toEntity() },
)

private fun FetchInterestsRecruitmentsResponse.InterestRecruitments.toEntity() =
    InterestsRecruitmentsEntity.InterestsRecruitmentEntity(
        id = this.id,
        companyId = this.companyId,
        companyProfileUrl = this.companyProfileUrl,
        trainPay = this.trainPay,
        militarySupport = this.militarySupport,
        hiringJobs = this.hiringJobs,
        bookmarked = this.bookmarked,
    )
