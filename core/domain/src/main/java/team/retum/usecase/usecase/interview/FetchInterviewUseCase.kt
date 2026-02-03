package team.retum.usecase.usecase.interview

import team.retum.data.repository.interview.InterviewRepository
import team.retum.usecase.entity.interview.toEntity
import javax.inject.Inject

class FetchInterviewUseCase @Inject constructor(
    private val interviewRepository: InterviewRepository,
) {
    suspend operator fun invoke() = runCatching {
        interviewRepository.fetchInterview().toEntity()
    }
}