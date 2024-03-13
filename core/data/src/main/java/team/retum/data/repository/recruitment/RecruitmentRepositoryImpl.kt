package team.retum.data.repository.recruitment

import team.retum.network.datasource.recruitment.RemoteRecruitmentDataSource
import team.retum.network.model.response.FetchRecruitmentDetailsResponse
import team.retum.network.model.response.FetchRecruitmentPageCountResponse
import team.retum.network.model.response.FetchRecruitmentsResponse
import javax.inject.Inject

class RecruitmentRepositoryImpl @Inject constructor(
    private val recruitmentDataSource: RemoteRecruitmentDataSource,
) : RecruitmentRepository {
    override suspend fun fetchRecruitments(
        page: Int,
        name: String?,
        jobCode: Long?,
        techCode: String?,
        winterIntern: Boolean,
    ): FetchRecruitmentsResponse =
        recruitmentDataSource.fetchRecruitments(
            page = page,
            name = name,
            jobCode = jobCode,
            techCode = techCode,
            winterIntern = winterIntern,
        )

    override suspend fun fetchRecruitmentPageCount(
        name: String?,
        jobCode: Long?,
        techCode: String?,
        winterIntern: Boolean,
    ): FetchRecruitmentPageCountResponse =
        recruitmentDataSource.fetchRecruitmentPageCount(
            name = name,
            jobCode = jobCode,
            techCode = techCode,
            winterIntern = winterIntern,
        )

    override suspend fun fetchRecruitmentDetails(
        recruitmentId: Long,
    ): FetchRecruitmentDetailsResponse =
        recruitmentDataSource.fetchRecruitmentDetails(recruitmentId = recruitmentId)
}
