package team.retum.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import team.retum.data.repository.BookmarkRepository
import team.retum.data.repository.BookmarkRepositoryImpl
import team.retum.data.repository.UserRepository
import team.retum.data.repository.UserRepositoryImpl
import team.retum.data.repository.company.CompanyRepository
import team.retum.data.repository.company.CompanyRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindsBookmarkRepository(bookmarkRepositoryImpl: BookmarkRepositoryImpl): BookmarkRepository

    @Binds
    @Singleton
    abstract fun bindCompanyRepository(companyRepositoryImpl: CompanyRepositoryImpl): CompanyRepository
}
