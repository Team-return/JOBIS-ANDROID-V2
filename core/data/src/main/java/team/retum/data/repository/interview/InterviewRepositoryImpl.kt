package team.retum.data.repository.interview

import team.retum.network.datasource.interview.InterviewDataSource
import team.retum.network.model.request.interview.InterviewRequest
import team.retum.network.model.response.interview.FetchInterviewResponse
import javax.inject.Inject

class InterviewRepositoryImpl @Inject constructor(
    private val interviewDataSource: InterviewDataSource,
) : InterviewRepository {
    override suspend fun setInterview(interview: InterviewRequest) {
        interviewDataSource.setInterview(interview = interview)
    }

    override suspend fun fetchInterview(): FetchInterviewResponse =
        interviewDataSource.fetchInterview()
}
