package team.retum.data.repository.auth

import team.retum.common.enums.AuthCodeType
import team.retum.network.datasource.auth.RemoteAuthDataSource
import team.retum.network.model.request.auth.SendAuthenticationCodeRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteAuthDataSource: RemoteAuthDataSource,
) : AuthRepository {
    override suspend fun sendAuthenticationCode(
        email: String,
        authCodeType: AuthCodeType,
    ) {
        remoteAuthDataSource.sendAuthenticationCode(
            sendAuthenticationCodeRequest = SendAuthenticationCodeRequest(
                email = email,
                authCodeType = authCodeType,
            )
        )
    }
}
