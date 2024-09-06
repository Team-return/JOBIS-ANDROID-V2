package team.retum.network.util

import okhttp3.Interceptor
import okhttp3.Response
import team.retum.common.utils.ResourceKeys
import team.retum.jobis.local.datasource.user.LocalUserDataSource
import team.retum.network.di.RequestUrls
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val localUserDataSource: LocalUserDataSource,
) : Interceptor {

    private lateinit var accessToken: String

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
        )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        return chain.proceed(
            if (ignorePaths.contains(path) || checkS3Request(url = request.url.toString())) {
                request
            } else {
                accessToken = localUserDataSource.getAccessToken()
                request.newBuilder()
                    .addHeader("Authorization", "${ResourceKeys.BEARER} $accessToken").build()
            },
        )
    }

    private fun checkS3Request(url: String): Boolean {
        return url.contains(ResourceKeys.IMAGE_URL)
    }
}
