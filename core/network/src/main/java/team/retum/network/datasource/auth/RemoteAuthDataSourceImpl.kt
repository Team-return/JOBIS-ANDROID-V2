package team.retum.network.datasource.auth

import team.retum.common.enums.PlatformType
import team.retum.network.api.AuthApi
import team.retum.network.model.request.auth.SendAuthenticationCodeRequest
import team.retum.network.model.response.TokenResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class RemoteAuthDataSourceImpl @Inject constructor(
    private val authApi: AuthApi
) : RemoteAuthDataSource {
    override suspend fun sendAuthenticationCode(sendAuthenticationCodeRequest: SendAuthenticationCodeRequest) {
        RequestHandler<Unit>().request {
            authApi.sendAuthenticationCode(sendAuthenticationCodeRequest = sendAuthenticationCodeRequest)
        }
    }

    override suspend fun authorizeAuthenticationCode(
        email: String,
        authCode: String,
    ) {
        RequestHandler<Unit>().request {
            authApi.authorizeAuthenticationCode(
                email = email,
                authCode = authCode,
            )
        }
    }

    override suspend fun reissueToken(
        refreshToken: String,
        platformType: PlatformType,
    ): TokenResponse {
        return RequestHandler<TokenResponse>().request {
            authApi.reissueToken(
                refreshToken = refreshToken,
                platformType = platformType,
            )
        }
    }
}
