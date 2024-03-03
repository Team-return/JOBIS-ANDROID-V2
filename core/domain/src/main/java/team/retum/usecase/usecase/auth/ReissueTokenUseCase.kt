package team.retum.usecase.usecase.auth

import team.retum.data.repository.auth.AuthRepository
import javax.inject.Inject

class ReissueTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(refreshToken: String) = runCatching {
        authRepository.reissueToken(refreshToken = refreshToken)
    }
}
