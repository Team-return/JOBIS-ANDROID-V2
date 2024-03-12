package team.retum.network.datasource.user

import team.retum.network.model.request.user.RegisterDeviceTokenRequest
import team.retum.network.model.request.user.SignInRequest
import team.retum.network.model.response.TokenResponse

interface RemoteUserDataSource {
    suspend fun signIn(signInRequest: SignInRequest): TokenResponse
}
