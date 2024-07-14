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

/**
 * accessToken의 재발급 과정 시 사용되는 클래스
 *
 * @property localUserDataSource 로컬 스토리지에서 사용자 토큰을 가져오기 위해 사용
 */
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

    /**
     * 요청 중 401 예외가 발생했을 때 스스로 동작
     *
     * @param route
     * @param response
     * @return 헤더에 재발급한 토큰을 추가한 새로운 request
     *
     * 토큰 재발급 api 요청 후 예외가 발생한다면 null request를 생성하여 반환
     */
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
