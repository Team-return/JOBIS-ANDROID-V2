package team.retum.data.repository.recruitment

import team.retum.network.model.response.FetchRecruitmentDetailsResponse
import team.retum.network.model.response.FetchRecruitmentPageCountResponse
import team.retum.network.model.response.FetchRecruitmentsResponse

interface RecruitmentRepository {
    suspend fun fetchRecruitments(
        page: Int,
        name: String?,
        jobCode: Long?,
        techCode: String?,
        winterIntern: Boolean,
        militarySupport: Boolean?,
        years: List<Int>?,
        recruitStatus: String?,
    ): FetchRecruitmentsResponse

    suspend fun fetchRecruitmentPageCount(
        name: String?,
        jobCode: Long?,
        techCode: String?,
        winterIntern: Boolean,
        militarySupport: Boolean?,
        years: List<Int>?,
        recruitStatus: String?,
    ): FetchRecruitmentPageCountResponse

    suspend fun fetchRecruitmentDetails(recruitmentId: Long): FetchRecruitmentDetailsResponse
}
