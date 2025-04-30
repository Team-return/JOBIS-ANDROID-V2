package team.retum.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import team.retum.network.di.RequestUrls
import team.retum.network.model.response.interests.FetchInterestsRecruitmentResponse
import team.retum.network.model.response.interests.FetchInterestsResponse

interface InterestsApi {
    @PATCH(RequestUrls.Interests.interests)
    suspend fun updateInterestsToggle(
        @Body codes: List<Int>,
    )

    @GET(RequestUrls.Interests.interests)
    suspend fun fetchInterests(): FetchInterestsResponse

    @GET(RequestUrls.Interests.interestsRecruitment)
    suspend fun fetchInterestsSearchRecruitments(): FetchInterestsRecruitmentResponse
}
