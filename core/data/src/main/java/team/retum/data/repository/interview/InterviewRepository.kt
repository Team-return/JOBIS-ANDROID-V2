package team.retum.data.repository.interview

import team.retum.network.model.request.interview.InterviewRequest
import team.retum.network.model.response.interview.FetchInterviewResponse

interface InterviewRepository {
    suspend fun setInterview(interview: InterviewRequest)
    suspend fun fetchInterview(): FetchInterviewResponse
}
