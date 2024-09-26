package team.retum.usecase.usecase.auth

import team.retum.common.utils.ResourceKeys
import team.retum.data.repository.auth.AuthRepository
import javax.inject.Inject

class AuthorizeAuthenticationCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        authCode: String,
    ) = runCatching {
        authRepository.authorizeAuthenticationCode(
            email = email + ResourceKeys.EMAIL,
            authCode = authCode,
        )
    }
}
