package team.retum.network.datasource.interest

import team.retum.network.api.InterestsApi
import team.retum.network.model.response.interests.FetchInterestsRecruitmentResponse
import team.retum.network.model.response.interests.FetchInterestsResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class InterestsDataSourceImpl @Inject constructor(
    private val interestsApi: InterestsApi,
) : InterestsDataSource {
    override suspend fun setInterestsToggle(codes: List<Int>) {
        RequestHandler<Unit>().request {
            interestsApi.setInterestsToggle(codes = codes)
        }
    }

    override suspend fun fetchInterests(): FetchInterestsResponse {
        return RequestHandler<FetchInterestsResponse>().request {
            interestsApi.fetchInterests()
        }
    }

    override suspend fun fetchInterestsSearchRecruitments(): FetchInterestsRecruitmentResponse {
        return RequestHandler<FetchInterestsRecruitmentResponse>().request {
            interestsApi.fetchInterestsSearchRecruitments()
        }
    }
}
