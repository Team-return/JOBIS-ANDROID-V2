package team.retum.network.datasource.interest

import team.retum.network.model.response.interests.FetchInterestsRecruitmentsResponse
import team.retum.network.model.response.interests.FetchInterestsResponse

interface InterestsDataSource {
    suspend fun setInterestsToggle(codes: List<Int>)
    suspend fun fetchInterests(): FetchInterestsResponse
    suspend fun fetchInterestsSearchRecruitments(): FetchInterestsRecruitmentsResponse
}
