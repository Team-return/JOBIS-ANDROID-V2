package team.retum.data.repository

import team.retum.network.datasource.UserDataSource
import team.retum.network.model.request.SignInRequest
import team.retum.network.model.response.TokenResponse
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
) : UserRepository {
    override suspend fun signIn(signInRequest: SignInRequest): TokenResponse {
        return userDataSource.signIn(signInRequest = signInRequest)
    }
}
