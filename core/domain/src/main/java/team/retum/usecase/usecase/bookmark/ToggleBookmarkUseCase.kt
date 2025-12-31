package team.retum.usecase.usecase.bookmark

import team.retum.data.repository.bookmark.BookmarkRepository
import team.retum.jobis.local.entity.BookmarkLocalEntity
import team.retum.usecase.entity.BookmarksEntity
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    suspend operator fun invoke(recruitmentId: BookmarkLocalEntity): Result<Unit> {
        return bookmarkRepository.toggleBookmark(recruitmentId)
    }
}
