package team.retum.usecase.usecase.interests

import team.retum.data.repository.interests.InterestsRepository
import javax.inject.Inject

class FetchInterestsSearchRecruitment @Inject constructor(
    private val interestsRepository: InterestsRepository,
) {
    suspend operator fun invoke() = runCatching {
        interestsRepository.fetchInterestsSearchRecruitments()
    }
}
