package team.retum.data.repository.auth

import team.retum.common.enums.AuthCodeType

interface AuthRepository {
    suspend fun sendAuthenticationCode(
        email: String,
        authCodeType: AuthCodeType,
    )
    suspend fun authorizeAuthenticationCode(
        email: String,
        authCode: String,
    )
    suspend fun reissueToken(refreshToken: String)
}
