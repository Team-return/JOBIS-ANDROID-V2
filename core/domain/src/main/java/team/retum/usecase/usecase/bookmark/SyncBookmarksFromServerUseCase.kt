package team.retum.usecase.usecase.bookmark

import team.retum.data.repository.bookmark.BookmarkRepository
import javax.inject.Inject

class SyncBookmarksFromServerUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        return bookmarkRepository.syncBookmarksFromServer()
    }
}
