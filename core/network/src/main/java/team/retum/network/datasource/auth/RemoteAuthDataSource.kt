package team.retum.network.datasource.auth

import team.retum.common.enums.PlatformType
import team.retum.network.model.request.auth.SendAuthenticationCodeRequest
import team.retum.network.model.response.TokenResponse

interface RemoteAuthDataSource {
    suspend fun sendAuthenticationCode(sendAuthenticationCodeRequest: SendAuthenticationCodeRequest)
    suspend fun authorizeAuthenticationCode(
        email: String,
        authCode: String,
    )
    suspend fun reissueToken(
        refreshToken: String,
        platformType: PlatformType,
    ): TokenResponse
}
