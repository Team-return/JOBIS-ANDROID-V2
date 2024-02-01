package team.retum.network.datasource

import team.retum.network.model.request.SignInRequest
import team.retum.network.model.response.TokenResponse

interface UserDataSource {
    suspend fun signIn(signInRequest: SignInRequest): TokenResponse
}
