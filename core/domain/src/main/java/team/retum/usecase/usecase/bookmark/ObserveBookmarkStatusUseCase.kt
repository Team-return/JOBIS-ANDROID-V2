package team.retum.usecase.usecase.bookmark

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import team.retum.data.repository.bookmark.BookmarkRepository
import team.retum.usecase.entity.BookmarksEntity
import team.retum.usecase.entity.toBookmarkEntities
import javax.inject.Inject

class ObserveBookmarkStatusUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    operator fun invoke(): Flow<List<BookmarksEntity.BookmarkEntity>> {
        return bookmarkRepository.observeAllBookmarks()
            .map { it.toBookmarkEntities() }
    }
}
