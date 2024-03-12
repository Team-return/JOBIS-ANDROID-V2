package team.retum.network.datasource.user

import team.retum.network.api.UserApi
import team.retum.network.model.request.user.SignInRequest
import team.retum.network.model.response.TokenResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class RemoteUserDataSourceImpl @Inject constructor(
    private val userApi: UserApi,
) : RemoteUserDataSource {
    override suspend fun signIn(signInRequest: SignInRequest): TokenResponse {
        return RequestHandler<TokenResponse>().request {
            userApi.signIn(signInRequest = signInRequest)
        }
    }
}
