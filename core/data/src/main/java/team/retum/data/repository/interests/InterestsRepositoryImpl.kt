package team.retum.data.repository.interests

import team.retum.network.datasource.interests.InterestsDataSource
import team.retum.network.model.response.interests.FetchInterestsRecruitmentsResponse
import team.retum.network.model.response.interests.FetchInterestsResponse
import javax.inject.Inject

class InterestsRepositoryImpl @Inject constructor(
    private val interestsDataSource: InterestsDataSource,
) : InterestsRepository {
    override suspend fun setInterestsToggle(codes: List<Int>) {
        interestsDataSource.setInterestsToggle(codes = codes)
    }

    override suspend fun fetchInterests(): FetchInterestsResponse =
        interestsDataSource.fetchInterests()

    override suspend fun fetchInterestsSearchRecruitments(): FetchInterestsRecruitmentsResponse =
        interestsDataSource.fetchInterestsSearchRecruitments()
}
