package team.retum.network.util

import okhttp3.Interceptor
import okhttp3.Response
import team.retum.network.di.RequestUrls

class TokenInterceptor : Interceptor {
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
        )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url().encodedPath()

        return chain.proceed(
            when (ignorePaths.contains(path)) {
                true -> request
                // TODO 토큰 캐싱 로직 구현
                false -> chain.request().newBuilder().addHeader("Authorization", "token").build()
            },
        )
    }
}
