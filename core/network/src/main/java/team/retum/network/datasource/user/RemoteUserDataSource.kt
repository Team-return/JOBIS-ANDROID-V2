package team.retum.network.datasource.user

import team.retum.network.model.request.SignInRequest
import team.retum.network.model.response.TokenResponse

interface RemoteUserDataSource {
    suspend fun signIn(signInRequest: SignInRequest): TokenResponse
}
