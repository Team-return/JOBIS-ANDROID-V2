package team.retum.usecase.intern

import team.retum.data.repository.intern.WinterInterRepository
import javax.inject.Inject

class FetchWinterInternUseCase @Inject constructor(
    private val winterInterRepository: WinterInterRepository,
) {
    suspend operator fun invoke() = runCatching {
        winterInterRepository.fetchWinterIntern()
    }
}
