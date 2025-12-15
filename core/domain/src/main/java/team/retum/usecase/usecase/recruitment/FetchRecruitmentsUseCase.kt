package team.retum.usecase.usecase.recruitment

import team.retum.common.enums.RecruitmentStatus
import team.retum.data.repository.recruitment.RecruitmentRepository
import team.retum.usecase.entity.toRecruitmentsEntity
import javax.inject.Inject

class FetchRecruitmentsUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository,
) {
    suspend operator fun invoke(
        page: Int,
        name: String?,
        jobCode: Long?,
        techCode: String?,
        winterIntern: Boolean,
        militarySupport: Boolean?,
        years: List<Int>?,
        recruitStatus: RecruitmentStatus?,
    ) = runCatching {
        recruitmentRepository.fetchRecruitments(
            page = page,
            name = name,
            jobCode = jobCode,
            techCode = techCode,
            winterIntern = winterIntern,
            militarySupport = militarySupport,
            years = years,
            recruitStatus = recruitStatus,
        ).toRecruitmentsEntity()
    }
}
