package team.retum.usecase.usecase.recruitment

import team.retum.data.repository.recruitment.RecruitmentRepository
import team.retum.usecase.entity.toRecruitmentCountEntity
import javax.inject.Inject

class FetchRecruitmentCountUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository,
) {
    suspend operator fun invoke(
        name: String?,
        jobCode: Long?,
        techCode: String?,
        winterIntern: Boolean,
    ) = runCatching {
        recruitmentRepository.fetchRecruitmentPageCount(
            name = name,
            jobCode = jobCode,
            techCode = techCode,
            winterIntern = winterIntern,
        ).toRecruitmentCountEntity()
    }
}
