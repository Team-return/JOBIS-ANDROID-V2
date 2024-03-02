package team.retum.network.datasource.recruitment

import team.retum.network.model.response.FetchRecruitmentPageCountResponse
import team.retum.network.model.response.FetchRecruitmentsResponse

interface RemoteRecruitmentDataSource {

    suspend fun fetchRecruitments(
        page: Int,
        name: String?,
        jobCode: Long?,
        techCode: String?,
        winterIntern: Boolean,
    ): FetchRecruitmentsResponse

    suspend fun fetchRecruitmentPageCount(
        name: String?,
        jobCode: Long?,
        techCode: String?,
        winterIntern: Boolean,
    ): FetchRecruitmentPageCountResponse
}
