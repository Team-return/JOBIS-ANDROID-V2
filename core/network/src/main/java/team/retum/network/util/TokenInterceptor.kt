package team.retum.network.util

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import team.retum.common.utils.ResourceKeys
import team.retum.jobis.local.datasource.user.LocalUserDataSource
import team.retum.network.di.RequestUrls
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val localUserDataSource: LocalUserDataSource,
) : Interceptor {

    private val ignorePaths by lazy {
        listOf(
            RequestUrls.Users.login,
            RequestUrls.Auth.reissue,
            RequestUrls.Auth.code,
            RequestUrls.Students.forgottenPassword,
            RequestUrls.Students.signUp,
            RequestUrls.Students.exists,
            RequestUrls.Files.delete,
            RequestUrls.Files.post,
            RequestUrls.Files.presignedUrl,
            RequestUrls.CheckServerStatus.checkServerStatus,
        )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val path = request.url.encodedPath

        var response = chain.proceed(
            if (ignorePaths.contains(path) || checkS3Request(url = request.url.toString())) {
                request
            } else {
                val accessToken = localUserDataSource.getAccessToken()
                request.newBuilder()
                    .addHeader("Authorization", "${ResourceKeys.BEARER} $accessToken")
                    .build()
            },
        )

        if (response.code == 401 && !ignorePaths.contains(path)) {
            response.close()

            val refreshToken = localUserDataSource.getRefreshToken()

            runBlocking {
                val newToken = RefreshTokenService.refreshToken(refreshToken)
                localUserDataSource.run {
                    saveAccessToken(newToken.accessToken)
                    saveAccessExpiresAt(newToken.accessExpiresAt)
                    saveRefreshToken(newToken.refreshToken)
                    saveRefreshExpiresAt(newToken.refreshExpiresAt)
                }

                request = request.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "${ResourceKeys.BEARER} ${newToken.accessToken}")
                    .build()

                response = chain.proceed(request)
            }
        }
        return response
    }

    private fun checkS3Request(url: String): Boolean {
        return url.contains(ResourceKeys.IMAGE_URL)
    }
}
