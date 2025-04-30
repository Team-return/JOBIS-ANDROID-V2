package team.retum.data.repository.interests

import team.retum.network.datasource.interest.InterestsDataSourceImpl
import team.retum.network.model.response.interests.FetchInterestsRecruitmentResponse
import team.retum.network.model.response.interests.FetchInterestsResponse
import javax.inject.Inject

class InterestsRepositoryImpl @Inject constructor(
    private val dataSourceImpl: InterestsDataSourceImpl,
) : InterestsRepository {
    override suspend fun updateInterestsToggle(codes: List<Int>) {
        dataSourceImpl.updateInterestsToggle(codes = codes)
    }

    override suspend fun fetchInterests(): FetchInterestsResponse =
        dataSourceImpl.fetchInterests()

    override suspend fun fetchInterestsSearchRecruitments(): FetchInterestsRecruitmentResponse =
        dataSourceImpl.fetchInterestsSearchRecruitments()

}
