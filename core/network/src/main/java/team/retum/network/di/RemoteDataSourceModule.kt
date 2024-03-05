package team.retum.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import team.retum.network.datasource.application.RemoteApplicationDataSource
import team.retum.network.datasource.application.RemoteApplicationDataSourceImpl
import team.retum.network.datasource.auth.RemoteAuthDataSource
import team.retum.network.datasource.auth.RemoteAuthDataSourceImpl
import team.retum.network.datasource.banner.RemoteBannerDataSource
import team.retum.network.datasource.banner.RemoteBannerDataSourceImpl
import team.retum.network.datasource.bookmark.BookmarkDataSource
import team.retum.network.datasource.bookmark.BookmarkDataSourceImpl
import team.retum.network.datasource.code.RemoteCodeDataSource
import team.retum.network.datasource.code.RemoteCodeDataSourceImpl
import team.retum.network.datasource.review.ReviewDataSource
import team.retum.network.datasource.review.ReviewDataSourceImpl
import team.retum.network.datasource.company.CompanyDataSource
import team.retum.network.datasource.company.CompanyDataSourceImpl
import team.retum.network.datasource.recruitment.RemoteRecruitmentDataSource
import team.retum.network.datasource.recruitment.RemoteRecruitmentDataSourceImpl
import team.retum.network.datasource.student.RemoteStudentDataSource
import team.retum.network.datasource.student.RemoteStudentDataSourceImpl
import team.retum.network.datasource.user.RemoteUserDataSource
import team.retum.network.datasource.user.RemoteUserDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindRemoteUserDataSource(remoteUserDataSourceImpl: RemoteUserDataSourceImpl): RemoteUserDataSource

    @Binds
    @Singleton
    abstract fun bindBookmarkDataSource(bookmarkDataSourceImpl: BookmarkDataSourceImpl): BookmarkDataSource

    @Binds
    @Singleton
    abstract fun bindCompanyDataSource(companyDataSourceImpl: CompanyDataSourceImpl): CompanyDataSource

    @Binds
    @Singleton
    abstract fun bindAuthDataSource(remoteAuthDataSourceImpl: RemoteAuthDataSourceImpl): RemoteAuthDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteStudentDataSource(remoteStudentDataSourceImpl: RemoteStudentDataSourceImpl): RemoteStudentDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteRecruitmentDataSource(remoteRecruitmentDataSourceImpl: RemoteRecruitmentDataSourceImpl): RemoteRecruitmentDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteCodeDataSource(remoteCodeDataSourceImpl: RemoteCodeDataSourceImpl): RemoteCodeDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteApplicationDataSource(remoteApplicationDataSourceImpl: RemoteApplicationDataSourceImpl): RemoteApplicationDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteBannerDataSource(remoteBannerDataSourceImpl: RemoteBannerDataSourceImpl): RemoteBannerDataSource

    @Binds
    @Singleton
    abstract fun bindReviewDataSource(reviewDataSourceImpl: ReviewDataSourceImpl): ReviewDataSource
}
