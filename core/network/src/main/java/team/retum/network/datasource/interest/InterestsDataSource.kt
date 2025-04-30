package team.retum.network.datasource.interest

import team.retum.network.model.response.interests.FetchInterestsRecruitmentResponse
import team.retum.network.model.response.interests.FetchInterestsResponse

interface InterestsDataSource {
    suspend fun updateInterestsToggle(codes: List<Int>)
    suspend fun fetchInterests(): FetchInterestsResponse
    suspend fun fetchInterestsSearchRecruitments(): FetchInterestsRecruitmentResponse
}
