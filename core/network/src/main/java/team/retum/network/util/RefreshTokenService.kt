package team.retum.network.util

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import team.retum.common.enums.PlatformType
import team.retum.network.BuildConfig
import team.retum.network.api.AuthApi
import team.retum.network.model.response.TokenResponse

object RefreshTokenService {

    private val refreshRetrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build(),
        )
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .addLast(KotlinJsonAdapterFactory())
                    .build(),
            ),
        )
        .build()

    private val refreshService = refreshRetrofit.create(AuthApi::class.java)

    suspend fun refreshToken(refreshToken: String): TokenResponse {
        return runCatching {
            refreshService.reissueToken(
                refreshToken = refreshToken,
                platformType = PlatformType.ANDROID,
            )
        }.onSuccess { token ->
            return token
        }.onFailure {
            throw IllegalStateException("Fail refresh")
        }.getOrThrow()
    }

}
