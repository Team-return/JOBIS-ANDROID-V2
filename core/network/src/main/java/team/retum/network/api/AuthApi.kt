package team.retum.network.api

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import team.retum.common.enums.PlatformType
import team.retum.network.di.RequestUrls
import team.retum.network.model.request.auth.SendAuthenticationCodeRequest
import team.retum.network.model.response.TokenResponse

interface AuthApi {
    @POST(RequestUrls.Auth.code)
    suspend fun sendAuthenticationCode(
        @Body sendAuthenticationCodeRequest: SendAuthenticationCodeRequest,
    )

    @PATCH(RequestUrls.Auth.code)
    suspend fun authorizeAuthenticationCode(
        @Query("email") email: String,
        @Query("auth_code") authCode: String,
    )

    @PUT(RequestUrls.Auth.reissue)
    suspend fun reissueToken(
        @Header("X-Refresh-Token") refreshToken: String,
        @Query("platform-type") platformType: PlatformType,
    ): TokenResponse
}
