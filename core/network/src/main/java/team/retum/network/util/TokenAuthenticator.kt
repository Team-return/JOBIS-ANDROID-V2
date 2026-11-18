package team.retum.network.util

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
import team.retum.network.di.RequestUrls
import team.retum.network.model.response.TokenResponse
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val localUserDataSource: LocalUserDataSource,
) : Authenticator {

    private val refreshMutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? =
        runBlocking {
            refreshMutex.withLock {
                if (
                    hasRetriedAuthentication(response) ||
                    response.request.url.encodedPath == RequestUrls.Auth.reissue
                ) {
                    signOut()
                    return@withLock null
                }

                val requestAccessToken = response.request.header(AUTHORIZATION)
                if (requestAccessToken == null) return@withLock null

                val accessToken = localUserDataSource.getAccessToken()
                if (accessToken.isBlank()) return@withLock null

                if (requestAccessToken != "${ResourceKeys.BEARER} $accessToken") {
                    return@withLock response.request.newBuilder()
                        .header(AUTHORIZATION, "${ResourceKeys.BEARER} $accessToken")
                        .build()
                }

                val tokenResponse = reissueToken() ?: run {
                    signOut()
                    return@withLock null
                }

                localUserDataSource.run {
                    saveAccessToken(tokenResponse.accessToken)
                    saveRefreshToken(tokenResponse.refreshToken)
                    saveAccessExpiresAt(tokenResponse.accessExpiresAt)
                    saveRefreshExpiresAt(tokenResponse.refreshExpiresAt)
                }

                response.request.newBuilder()
                    .header(AUTHORIZATION, "${ResourceKeys.BEARER} ${tokenResponse.accessToken}")
                    .build()
            }
        }

    private suspend fun reissueToken(): TokenResponse? = runCatching {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
            .reissueToken(
                refreshToken = localUserDataSource.getRefreshToken(),
                platformType = PlatformType.ANDROID,
            )
    }.getOrNull()

    private fun signOut() {
        localUserDataSource.clearUserInformation()
        context.packageManager.getLaunchIntentForPackage(context.packageName)
            ?.apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            ?.let(context::startActivity)
    }

    private fun hasRetriedAuthentication(response: Response): Boolean {
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            if (priorResponse.code == HTTP_UNAUTHORIZED) return true
            priorResponse = priorResponse.priorResponse
        }
        return false
    }

    private companion object {
        const val AUTHORIZATION = "Authorization"
        const val HTTP_UNAUTHORIZED = 401
    }
}
