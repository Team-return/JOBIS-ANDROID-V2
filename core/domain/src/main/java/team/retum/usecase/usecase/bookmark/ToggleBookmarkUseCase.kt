package team.retum.usecase.usecase.bookmark

import team.retum.data.repository.bookmark.BookmarkRepository
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    suspend operator fun invoke(recruitmentId: Long): Result<Unit> {
        return bookmarkRepository.toggleBookmark(recruitmentId)
    }
}
