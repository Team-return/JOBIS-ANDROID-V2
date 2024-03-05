package team.retum.network.datasource.recruitment

import team.retum.network.api.RecruitmentApi
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
    ): FetchRecruitmentsResponse = RequestHandler<FetchRecruitmentsResponse>().request {
        recruitmentApi.fetchRecruitments(
            page = page,
            name = name,
            jobCode = jobCode,
            techCode = techCode,
            winterIntern = winterIntern,
        )
    }

    override suspend fun fetchRecruitmentPageCount(
        name: String?,
        jobCode: Long?,
        techCode: String?,
        winterIntern: Boolean,
    ): FetchRecruitmentPageCountResponse =
        RequestHandler<FetchRecruitmentPageCountResponse>().request {
            recruitmentApi.fetchRecruitmentPageCount(
                name = name,
                jobCode = jobCode,
                techCode = techCode,
                winterIntern = winterIntern,
            )
        }
}
