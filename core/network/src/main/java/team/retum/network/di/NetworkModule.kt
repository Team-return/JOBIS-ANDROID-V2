package team.retum.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import team.retum.jobis.local.datasource.user.LocalUserDataSource
import team.retum.network.BuildConfig
import team.retum.network.api.ApplicationApi
import team.retum.network.FileApi
import team.retum.network.api.AuthApi
import team.retum.network.api.BannerApi
import team.retum.network.api.BookmarkApi
import team.retum.network.api.CodeApi
import team.retum.network.api.CompanyApi
import team.retum.network.api.RecruitmentApi
import team.retum.network.api.StudentApi
import team.retum.network.api.UserApi
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
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .addInterceptor(getLoggingInterceptor())
            .build()
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
    fun provideFileApi(retrofit: Retrofit): FileApi {
        return retrofit.create(FileApi::class.java)
    }
}
