package team.retum.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import team.retum.network.di.RequestUrls
import team.retum.network.model.request.interests.InterestsToggleRequest
import team.retum.network.model.response.interests.FetchInterestsRecruitmentsResponse
import team.retum.network.model.response.interests.FetchInterestsResponse

interface InterestsApi {
    @PATCH(RequestUrls.Interests.interests)
    suspend fun setInterestsToggle(
        @Body codes: InterestsToggleRequest,
    )

    @GET(RequestUrls.Interests.interests)
    suspend fun fetchInterests(): FetchInterestsResponse

    @GET(RequestUrls.Interests.interestsRecruitments)
    suspend fun fetchInterestsSearchRecruitments(): FetchInterestsRecruitmentsResponse
}
