package team.retum.usecase.usecase.interests

import team.retum.data.repository.interests.InterestsRepository
import team.retum.network.model.request.interests.InterestsToggleRequest
import javax.inject.Inject

class SetInterestsToggleUseCase @Inject constructor(
    private val interestsRepository: InterestsRepository,
) {
    suspend operator fun invoke(codes: InterestsToggleRequest) = runCatching {
        interestsRepository.setInterestsToggle(codes = codes)
    }
}
