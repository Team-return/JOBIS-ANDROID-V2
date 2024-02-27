package team.retum.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import team.retum.data.repository.auth.AuthRepository
import team.retum.data.repository.auth.AuthRepositoryImpl
import team.retum.data.repository.bookmark.BookmarkRepository
import team.retum.data.repository.bookmark.BookmarkRepositoryImpl
import team.retum.data.repository.company.CompanyRepository
import team.retum.data.repository.company.CompanyRepositoryImpl
import team.retum.data.repository.student.StudentRepository
import team.retum.data.repository.student.StudentRepositoryImpl
import team.retum.data.repository.user.UserRepository
import team.retum.data.repository.user.UserRepositoryImpl
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

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindStudentRepository(studentRepositoryImpl: StudentRepositoryImpl): StudentRepository
}
