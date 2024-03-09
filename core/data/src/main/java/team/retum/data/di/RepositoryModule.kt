package team.retum.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import team.retum.data.repository.application.ApplicationRepository
import team.retum.data.repository.application.ApplicationRepositoryImpl
import team.retum.data.repository.auth.AuthRepository
import team.retum.data.repository.auth.AuthRepositoryImpl
import team.retum.data.repository.banner.BannerRepository
import team.retum.data.repository.banner.BannerRepositoryImpl
import team.retum.data.repository.bookmark.BookmarkRepository
import team.retum.data.repository.bookmark.BookmarkRepositoryImpl
import team.retum.data.repository.code.CodeRepository
import team.retum.data.repository.code.CodeRepositoryImpl
import team.retum.data.repository.review.ReviewRepository
import team.retum.data.repository.review.ReviewRepositoryImpl
import team.retum.data.repository.company.CompanyRepository
import team.retum.data.repository.company.CompanyRepositoryImpl
import team.retum.data.repository.notification.NotificationRepository
import team.retum.data.repository.notification.NotificationRepositoryImpl
import team.retum.data.repository.file.FileRepository
import team.retum.data.repository.file.FileRepositoryImpl
import team.retum.data.repository.notice.NoticeRepository
import team.retum.data.repository.notice.NoticeRepositoryImpl
import team.retum.data.repository.recruitment.RecruitmentRepository
import team.retum.data.repository.recruitment.RecruitmentRepositoryImpl
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

    @Binds
    @Singleton
    abstract fun bindRecruitmentRepository(recruitmentRepositoryImpl: RecruitmentRepositoryImpl): RecruitmentRepository

    @Binds
    @Singleton
    abstract fun bindCodeRepository(codeRepositoryImpl: CodeRepositoryImpl): CodeRepository

    @Binds
    @Singleton
    abstract fun bindApplicationRepository(applicationRepositoryImpl: ApplicationRepositoryImpl): ApplicationRepository

    @Binds
    @Singleton
    abstract fun bindBannerRepository(bannerRepositoryImpl: BannerRepositoryImpl): BannerRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(notificationRepositoryImpl: NotificationRepositoryImpl): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindReviewRepository(reviewRepositoryImpl: ReviewRepositoryImpl): ReviewRepository

    @Binds
    @Singleton
    abstract fun bindFileRepository(fileRepositoryImpl: FileRepositoryImpl): FileRepository

    @Binds
    @Singleton
    abstract fun bindNoticeRepository(noticeRepositoryImpl: NoticeRepositoryImpl): NoticeRepository
}
