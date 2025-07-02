package team.retum.network.datasource.interests

import team.retum.network.api.InterestsApi
import team.retum.network.model.request.interests.InterestsToggleRequest
import team.retum.network.model.response.interests.FetchInterestsRecruitmentsResponse
import team.retum.network.model.response.interests.FetchInterestsResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class InterestsDataSourceImpl @Inject constructor(
    private val interestsApi: InterestsApi,
) : InterestsDataSource {
    override suspend fun setInterestsToggle(codes: InterestsToggleRequest) {
        RequestHandler<Unit>().request {
            interestsApi.setInterestsToggle(codes = codes)
        }
    }

    override suspend fun fetchInterests(): FetchInterestsResponse {
        return RequestHandler<FetchInterestsResponse>().request {
            interestsApi.fetchInterests()
        }
    }

    override suspend fun fetchInterestsSearchRecruitments(): FetchInterestsRecruitmentsResponse {
        return RequestHandler<FetchInterestsRecruitmentsResponse>().request {
            interestsApi.fetchInterestsSearchRecruitments()
        }
    }
}
