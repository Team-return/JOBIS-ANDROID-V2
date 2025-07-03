package team.retum.network.datasource.interests

import team.retum.network.model.request.interests.InterestsToggleRequest
import team.retum.network.model.response.interests.FetchInterestsRecruitmentsResponse
import team.retum.network.model.response.interests.FetchInterestsResponse

interface InterestsDataSource {
    suspend fun setInterestsToggle(codes: InterestsToggleRequest)
    suspend fun fetchInterests(): FetchInterestsResponse
    suspend fun fetchInterestsSearchRecruitments(): FetchInterestsRecruitmentsResponse
}
