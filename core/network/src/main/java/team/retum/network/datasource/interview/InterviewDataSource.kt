package team.retum.network.datasource.interview

import team.retum.network.model.request.interview.InterviewRequest
import team.retum.network.model.response.interview.FetchInterviewResponse

interface InterviewDataSource {
    suspend fun setInterview(interview: InterviewRequest)
    suspend fun fetchInterview(): FetchInterviewResponse
}