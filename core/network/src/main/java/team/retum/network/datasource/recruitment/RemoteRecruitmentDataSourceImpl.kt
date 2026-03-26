package team.retum.network.datasource.recruitment

import team.retum.common.enums.RecruitmentStatus
import team.retum.network.api.RecruitmentApi
import team.retum.network.model.response.FetchRecruitmentDetailsResponse
import team.retum.network.model.response.FetchRecruitmentPageCountResponse
import team.retum.network.model.response.FetchRecruitmentsResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class RemoteRecruitmentDataSourceImpl @Inject constructor(
    private val recruitmentApi: RecruitmentApi,
) : RemoteRecruitmentDataSource {
    override suspend fun fetchRecruitments(
        page: Int,
        name: String?,
        jobCode: Long?,
        techCode: String?,
        winterIntern: Boolean,
        militarySupport: Boolean?,
        years: List<Int>?,
        recruitStatus: RecruitmentStatus?,
        sortType: String?,
        region: String?,
    ): FetchRecruitmentsResponse = RequestHandler<FetchRecruitmentsResponse>().request {
        recruitmentApi.fetchRecruitments(
            page = page,
            name = name,
            jobCode = jobCode,
            techCode = techCode,
            winterIntern = winterIntern,
            militarySupport = militarySupport,
            years = years,
            recruitStatus = recruitStatus?.name,
            sortType = sortType,
            region = region,
        )
    }

    override suspend fun fetchRecruitmentPageCount(
        name: String?,
        jobCode: Long?,
        techCode: String?,
        winterIntern: Boolean,
        militarySupport: Boolean?,
        years: List<Int>?,
        recruitStatus: RecruitmentStatus?,
        sortType: String?,
        region: String?,
    ): FetchRecruitmentPageCountResponse =
        RequestHandler<FetchRecruitmentPageCountResponse>().request {
            recruitmentApi.fetchRecruitmentPageCount(
                name = name,
                jobCode = jobCode,
                techCode = techCode,
                winterIntern = winterIntern,
                militarySupport = militarySupport,
                years = years,
                recruitStatus = recruitStatus?.name,
                sortType = sortType,
                region = region,
            )
        }

    override suspend fun fetchRecruitmentDetails(recruitmentId: Long): FetchRecruitmentDetailsResponse =
        RequestHandler<FetchRecruitmentDetailsResponse>().request {
            recruitmentApi.fetchRecruitmentDetails(recruitmentId = recruitmentId)
        }
}
