package team.retum.usecase.usecase.recruitment

import team.retum.data.repository.recruitment.RecruitmentRepository
import team.retum.usecase.entity.toRecruitmentDetailsEntity
import javax.inject.Inject

class FetchRecruitmentDetailsUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository,
) {
    suspend operator fun invoke(
        recruitmentId: Long,
    ) = runCatching {
        recruitmentRepository.fetchRecruitmentDetails(
            recruitmentId = recruitmentId,
        ).toRecruitmentDetailsEntity()
    }
}
