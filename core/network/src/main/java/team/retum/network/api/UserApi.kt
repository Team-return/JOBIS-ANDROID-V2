package team.retum.network.api

import retrofit2.http.Body
import retrofit2.http.POST
import team.retum.network.di.RequestUrls
import team.retum.network.model.request.user.SignInRequest
import team.retum.network.model.response.TokenResponse

interface UserApi {
    @POST(RequestUrls.Users.login)
    suspend fun signIn(
        @Body signInRequest: SignInRequest,
    ): TokenResponse
}
