package team.retum.network.datasource.auth

import team.retum.network.api.AuthApi
import team.retum.network.model.request.auth.AuthorizeAuthenticationCodeRequest
import team.retum.network.model.request.auth.SendAuthenticationCodeRequest
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

    override suspend fun authorizeAuthenticationCode(authorizeAuthenticationCodeRequest: AuthorizeAuthenticationCodeRequest) {
        RequestHandler<Unit>().request {
            authApi.authorizeAuthenticationCode(authorizeAuthenticationCodeRequest = authorizeAuthenticationCodeRequest)
        }
    }
}
