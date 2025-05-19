package team.retum.data.repository.interests

import team.retum.network.model.response.interests.FetchInterestsRecruitmentsResponse
import team.retum.network.model.response.interests.FetchInterestsResponse

interface InterestsRepository {
    suspend fun setInterestsToggle(codes: List<Long>)
    suspend fun fetchInterests(): FetchInterestsResponse
    suspend fun fetchInterestsSearchRecruitments(): FetchInterestsRecruitmentsResponse
}
