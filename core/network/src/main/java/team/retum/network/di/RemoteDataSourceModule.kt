package team.retum.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import team.retum.network.datasource.bookmark.BookmarkDataSource
import team.retum.network.datasource.bookmark.BookmarkDataSourceImpl
import team.retum.network.datasource.company.CompanyDataSource
import team.retum.network.datasource.company.CompanyDataSourceImpl
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
}
