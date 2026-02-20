package team.retum.usecase.usecase.interview

import team.retum.common.enums.HiringProgress
import team.retum.data.repository.interview.InterviewRepository
import team.retum.network.model.request.interview.InterviewRequest
import javax.inject.Inject

class SetInterviewUseCase @Inject constructor(
    private val interviewRepository: InterviewRepository,
) {
    suspend operator fun invoke(
        interviewType: HiringProgress,
        startDate: String,
        endDate: String,
        interviewTime: String,
        companyName: String,
        location: String,
    ) = runCatching {
        interviewRepository.setInterview(
            interview = InterviewRequest(
                interviewType = interviewType,
                startDate = startDate,
                endDate = endDate,
                interviewTime = interviewTime,
                companyName = companyName,
                location = location,
            ),
        )
    }
}
