package team.retum.data.repository.auth

import team.retum.common.enums.AuthCodeType
import team.retum.common.enums.PlatformType
import team.retum.jobis.local.datasource.user.LocalUserDataSource
import team.retum.network.datasource.auth.RemoteAuthDataSource
import team.retum.network.model.request.auth.SendAuthenticationCodeRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteAuthDataSource: RemoteAuthDataSource,
    private val localUserDataSource: LocalUserDataSource,
) : AuthRepository {
    override suspend fun sendAuthenticationCode(
        email: String,
        authCodeType: AuthCodeType,
    ) {
        remoteAuthDataSource.sendAuthenticationCode(
            sendAuthenticationCodeRequest = SendAuthenticationCodeRequest(
                email = email,
                authCodeType = authCodeType,
            ),
        )
    }

    override suspend fun authorizeAuthenticationCode(
        email: String,
        authCode: String,
    ) {
        remoteAuthDataSource.authorizeAuthenticationCode(
            email = email,
            authCode = authCode,
        )
    }

    override suspend fun reissueToken(refreshToken: String) {
        val response = remoteAuthDataSource.reissueToken(
            refreshToken = refreshToken,
            platformType = PlatformType.ANDROID,
        )
        localUserDataSource.run {
            saveAccessToken(response.accessToken)
            saveAccessExpiresAt(response.accessExpiresAt)
            saveRefreshToken(response.refreshToken)
            saveRefreshExpiresAt(response.refreshExpiresAt)
        }
    }
}
