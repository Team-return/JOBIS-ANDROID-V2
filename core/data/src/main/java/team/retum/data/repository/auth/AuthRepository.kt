package team.retum.data.repository.auth

import team.retum.common.enums.AuthCodeType

interface AuthRepository {
    suspend fun sendAuthenticationCode(
        email: String,
        authCodeType: AuthCodeType,
    )
}
