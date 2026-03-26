package team.retum.usecase.usecase.recruitment

import team.retum.common.enums.RecruitSortType
import team.retum.common.enums.RecruitmentRegion
import team.retum.common.enums.RecruitmentStatus
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
        militarySupport: Boolean?,
        years: List<Int>?,
        recruitStatus: RecruitmentStatus?,
        sortType: RecruitSortType?,
        region: RecruitmentRegion?,
    ) = runCatching {
        recruitmentRepository.fetchRecruitmentPageCount(
            name = name,
            jobCode = jobCode,
            techCode = techCode,
            winterIntern = winterIntern,
            militarySupport = militarySupport,
            years = years,
            recruitStatus = recruitStatus,
            sortType = sortType?.name,
            region = region?.name,
        ).toRecruitmentCountEntity()
    }
}
