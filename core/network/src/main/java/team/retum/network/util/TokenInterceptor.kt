package team.retum.network.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.Retrofit
import team.retum.common.enums.PlatformType
import team.retum.common.utils.ResourceKeys
import team.retum.jobis.local.datasource.user.LocalUserDataSource
import team.retum.network.BuildConfig
import team.retum.network.api.AuthApi
import team.retum.network.di.RequestUrls
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val localUserDataSource: LocalUserDataSource,
    //private val retrofit: Retrofit,
    //private val authApi: AuthApi,
) : Interceptor {

    //private lateinit var accessToken: String

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

        var response =  chain.proceed(
            if (ignorePaths.contains(path) || checkS3Request(url = request.url.toString())) {
                request
            } else {
                val accessToken = localUserDataSource.getAccessToken()
                request.newBuilder()
                    .addHeader("Authorization", "${ResourceKeys.BEARER} $accessToken")
                    .build()
            },
        )

        if(response.code == 401 && !ignorePaths.contains(path)) {
            response.close()

            val refreshToken = localUserDataSource.getRefreshToken()

            val newAccessToken = RefreshTokenService.refreshToken(refreshToken)
            if(newAccessToken != null) {
//                request = request.newBuilder()
//                    .url("${BuildConfig.BASE_URL}/auth/reissue?platform-type='Android'")
//                    .header("X-Refresh-Token","")
//                    .put(RequestBody.create(null,""))
//                    .build()
                request = request.newBuilder()
                    .removeHeader("X-Refresh-Token")
                    .addHeader("X-Refresh-Token", "${ResourceKeys.BEARER} $newAccessToken")
                    .build()

                response = chain.proceed(request)
            }
        }

        return response
    }

    private fun checkS3Request(url: String): Boolean {
        return url.contains(ResourceKeys.IMAGE_URL)
    }

    private fun reissueToken(): String? {
        val refreshToken = localUserDataSource.getRefreshToken()

        var token: String? = null
        CoroutineScope(Dispatchers.IO).launch {
//            runCatching {
//                authApi.reissueToken(
//                    refreshToken = refreshToken,
//                    platformType = PlatformType.ANDROID,
//                )
//            }.onSuccess {
//               token = it.accessToken
//            }.onFailure {
//                token = null
//            }
        }
        return token
    }
}
