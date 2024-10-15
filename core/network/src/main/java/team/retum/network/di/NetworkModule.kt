package team.retum.network.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import team.retum.jobis.local.datasource.user.LocalUserDataSource
import team.retum.network.BuildConfig
import team.retum.network.api.ApplicationApi
import team.retum.network.api.AuthApi
import team.retum.network.api.BannerApi
import team.retum.network.api.BookmarkApi
import team.retum.network.api.BugApi
import team.retum.network.api.CodeApi
import team.retum.network.api.CompanyApi
import team.retum.network.api.FileApi
import team.retum.network.api.NoticeApi
import team.retum.network.api.NotificationApi
import team.retum.network.api.RecruitmentApi
import team.retum.network.api.ReviewApi
import team.retum.network.api.ServerStatusCheckApi
import team.retum.network.api.StudentApi
import team.retum.network.api.UserApi
import team.retum.network.api.WinterInternApi
import team.retum.network.util.TokenAuthenticator
import team.retum.network.util.TokenInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private fun getLoggingInterceptor() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(moshiConverterFactory)
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(): MoshiConverterFactory {
        return MoshiConverterFactory.create(
            Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build(),
        )
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenInterceptor: Interceptor,
        authenticator: Authenticator,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .addInterceptor(getLoggingInterceptor())
            .authenticator(authenticator)
            .build()
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(localUserDataSource: LocalUserDataSource): Authenticator {
        return TokenAuthenticator(localUserDataSource = localUserDataSource)
    }

    @Provides
    @Singleton
    fun provideTokenInterceptor(localUserDataSource: LocalUserDataSource): Interceptor {
        return TokenInterceptor(localUserDataSource = localUserDataSource)
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBookmarkApi(retrofit: Retrofit): BookmarkApi {
        return retrofit.create(BookmarkApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCompanyApi(retrofit: Retrofit): CompanyApi {
        return retrofit.create(CompanyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStudentApi(retrofit: Retrofit): StudentApi {
        return retrofit.create(StudentApi::class.java)
    }

    @Provides
    @Singleton
    fun providesRecruitmentApi(retrofit: Retrofit): RecruitmentApi {
        return retrofit.create(RecruitmentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCodeApi(retrofit: Retrofit): CodeApi {
        return retrofit.create(CodeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideApplicationApi(retrofit: Retrofit): ApplicationApi {
        return retrofit.create(ApplicationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBannerApi(retrofit: Retrofit): BannerApi {
        return retrofit.create(BannerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationAPi(retrofit: Retrofit): NotificationApi {
        return retrofit.create(NotificationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideReviewApi(retrofit: Retrofit): ReviewApi {
        return retrofit.create(ReviewApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFileApi(retrofit: Retrofit): FileApi {
        return retrofit.create(FileApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNoticeApi(retrofit: Retrofit): NoticeApi {
        return retrofit.create(NoticeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBugApi(retrofit: Retrofit): BugApi {
        return retrofit.create(BugApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWinterInternApi(retrofit: Retrofit): WinterInternApi {
        return retrofit.create(WinterInternApi::class.java)
    }

    @Provides
    @Singleton
    fun provideServerStatusCheckApi(retrofit: Retrofit): ServerStatusCheckApi {
        return retrofit.create(ServerStatusCheckApi::class.java)
    }
}
