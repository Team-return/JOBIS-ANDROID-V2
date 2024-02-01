package team.retum.network.datasource

import team.retum.network.api.UserApi
import team.retum.network.model.request.SignInRequest
import team.retum.network.model.response.TokenResponse
import team.retum.network.util.RequestHandler
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val userApi: UserApi,
) : UserDataSource {
    override suspend fun signIn(signInRequest: SignInRequest): TokenResponse {
        return RequestHandler<TokenResponse>().request {
            userApi.signIn(signInRequest = signInRequest)
        }
    }
}
