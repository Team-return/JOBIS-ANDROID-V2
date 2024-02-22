package team.retum.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import team.retum.network.datasource.BookmarkDataSource
import team.retum.network.datasource.BookmarkDataSourceImpl
import team.retum.network.datasource.UserDataSource
import team.retum.network.datasource.UserDataSourceImpl
import team.retum.network.datasource.company.CompanyDataSource
import team.retum.network.datasource.company.CompanyDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindUserDataSource(userDataSourceImpl: UserDataSourceImpl): UserDataSource

    @Binds
    @Singleton
    abstract fun bindBookmarkDataSource(bookmarkDataSourceImpl: BookmarkDataSourceImpl): BookmarkDataSource

    @Binds
    @Singleton
    abstract fun bindCompanyDataSource(companyDataSourceImpl: CompanyDataSourceImpl): CompanyDataSource
}
