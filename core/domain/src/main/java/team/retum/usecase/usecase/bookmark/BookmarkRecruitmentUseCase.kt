package team.retum.usecase.usecase.bookmark

import team.retum.data.repository.BookmarkRepository
import javax.inject.Inject

class BookmarkRecruitmentUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    suspend operator fun invoke(recruitmentId: Long) = runCatching {
        bookmarkRepository.bookmarkRecruitment(recruitmentId)
    }
}
