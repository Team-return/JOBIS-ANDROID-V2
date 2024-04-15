package team.retum.usecase.usecase.auth

import team.retum.common.enums.AuthCodeType
import team.retum.common.utils.ResourceKeys
import team.retum.data.repository.auth.AuthRepository
import javax.inject.Inject

class SendAuthenticationCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        authCodeType: AuthCodeType,
    ) = runCatching {
        authRepository.sendAuthenticationCode(
            email = email + ResourceKeys.EMAIL,
            authCodeType = authCodeType,
        )
    }
}
