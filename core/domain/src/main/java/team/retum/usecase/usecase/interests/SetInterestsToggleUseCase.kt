package team.retum.usecase.usecase.interests

import team.retum.data.repository.interests.InterestsRepository
import javax.inject.Inject

class SetInterestsToggleUseCase @Inject constructor(
    private val interestsRepository: InterestsRepository,
) {
    suspend operator fun invoke(codes: Int) = runCatching {
        interestsRepository.setInterestsToggle(codes = codes)
    }
}
