package team.retum.network.datasource.interview

import team.retum.network.api.InterviewApi
import team.retum.network.model.request.interview.InterviewRequest
import team.retum.network.model.response.interview.FetchInterviewResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class InterviewDataSourceImpl @Inject constructor(
    private val interviewApi: InterviewApi,
) : InterviewDataSource {
    override suspend fun setInterview(interview: InterviewRequest) {
        RequestHandler<Unit>().request {
            interviewApi.setInterview(interview = interview)
        }
    }

    override suspend fun fetchInterview(): FetchInterviewResponse {
        return RequestHandler<FetchInterviewResponse>().request {
            interviewApi.fetchInterview()
        }
    }
}
