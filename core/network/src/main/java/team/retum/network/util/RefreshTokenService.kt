package team.retum.network.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import team.retum.common.enums.PlatformType
import team.retum.network.BuildConfig
import team.retum.network.api.AuthApi

object RefreshTokenService {

    private val refreshRetrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build(),
        )
        .build()

    private val refreshService = refreshRetrofit.create(AuthApi::class.java)

    fun refreshToken(
        refreshToken: String,
    ): String {
        var token = ""

        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                refreshService.reissueToken(
                    refreshToken = refreshToken,
                    platformType = PlatformType.ANDROID,
                )
            }.onSuccess {
                token = it.refreshToken
            }.onFailure {
                token = "null"
            }
        }
        return token
    }

}
