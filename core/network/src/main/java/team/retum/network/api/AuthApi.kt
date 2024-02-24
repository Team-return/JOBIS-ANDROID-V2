package team.retum.network.api

import retrofit2.http.Body
import retrofit2.http.POST
import team.retum.network.di.RequestUrls
import team.retum.network.model.request.auth.SendAuthenticationCodeRequest

interface AuthApi {
    @POST(RequestUrls.Auth.code)
    suspend fun sendAuthenticationCode(
        @Body sendAuthenticationCodeRequest: SendAuthenticationCodeRequest,
    )
}
