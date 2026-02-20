package team.retum.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import team.retum.network.di.RequestUrls
import team.retum.network.model.request.interview.InterviewRequest
import team.retum.network.model.response.interview.FetchInterviewResponse

interface InterviewApi {
    @POST(RequestUrls.Interviews.interviews)
    suspend fun setInterview(
        @Body interview: InterviewRequest,
    )

    @GET(RequestUrls.Interviews.interviews)
    suspend fun fetchInterview(): FetchInterviewResponse
}
