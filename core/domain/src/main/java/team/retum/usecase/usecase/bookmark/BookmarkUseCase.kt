package team.retum.usecase.usecase.bookmark

import team.retum.data.repository.BookmarkRepository
import team.retum.usecase.entity.toBookmarkEntity
import javax.inject.Inject

class BookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    suspend operator fun invoke() = runCatching {
        bookmarkRepository.fetchBookmarks().toBookmarkEntity()
    }
}
