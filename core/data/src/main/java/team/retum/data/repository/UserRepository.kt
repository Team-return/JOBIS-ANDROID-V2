package team.retum.data.repository

import team.retum.network.model.request.SignInRequest
import team.retum.network.model.response.TokenResponse

interface UserRepository {
    suspend fun signIn(signInRequest: SignInRequest): TokenResponse
}
