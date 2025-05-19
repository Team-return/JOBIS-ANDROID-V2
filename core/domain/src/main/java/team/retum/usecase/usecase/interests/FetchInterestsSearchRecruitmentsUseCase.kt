package team.retum.usecase.usecase.interests

import team.retum.data.repository.interests.InterestsRepository
import team.retum.usecase.entity.interests.toEntity
import javax.inject.Inject

class FetchInterestsSearchRecruitmentsUseCase @Inject constructor(
    private val interestsRepository: InterestsRepository,
) {
    suspend operator fun invoke() = runCatching {
        interestsRepository.fetchInterestsSearchRecruitments().toEntity()
    }
}
