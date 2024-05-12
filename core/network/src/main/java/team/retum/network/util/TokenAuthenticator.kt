package team.retum.network.util

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import team.retum.common.enums.PlatformType
import team.retum.common.utils.ResourceKeys
import team.retum.jobis.local.datasource.user.LocalUserDataSource
import team.retum.network.BuildConfig
import team.retum.network.api.AuthApi
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val localUserDataSource: LocalUserDataSource,
) : Authenticator {

    private val accessToken by lazy {
        localUserDataSource.getAccessToken()
    }

    private val refreshToken by lazy {
        localUserDataSource.getRefreshToken()
    }

    private var hasException: Boolean = false

    override fun authenticate(route: Route?, response: Response): Request? {
        val retrofit = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create()).build()
        val authApi = retrofit.create(AuthApi::class.java)

        runBlocking {
            runCatching {
                authApi.reissueToken(
                    refreshToken = refreshToken,
                    platformType = PlatformType.ANDROID,
                )
            }.onSuccess {
                with(localUserDataSource) {
                    saveAccessToken(it.accessToken)
                    saveRefreshToken(it.refreshToken)
                    saveAccessExpiresAt(it.accessExpiresAt)
                    saveRefreshExpiresAt(it.refreshExpiresAt)
                }
            }.onFailure {
                hasException = true
            }
        }

        return buildRequest(response = response)
    }

    private fun buildRequest(response: Response): Request? {
        return if (hasException) null
        else response.request.newBuilder()
            .addHeader("Authorization", "${ResourceKeys.BEARER} $accessToken")
            .build()
    }
}
