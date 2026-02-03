package team.retum.usecase.usecase.interview

import team.retum.data.repository.interview.InterviewRepository
import team.retum.network.model.request.interview.InterviewRequest
import javax.inject.Inject

class SetInterviewUseCase @Inject constructor(
    private val interviewRepository: InterviewRepository,
) {
    suspend operator fun invoke(interview: InterviewRequest) = runCatching {
        interviewRepository.setInterview(interview = interview)
    }
}