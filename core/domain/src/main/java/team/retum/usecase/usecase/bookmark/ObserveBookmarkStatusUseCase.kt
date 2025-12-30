package team.retum.usecase.usecase.bookmark

import kotlinx.coroutines.flow.Flow
import team.retum.data.repository.bookmark.BookmarkRepository
import javax.inject.Inject

class ObserveBookmarkStatusUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) {
    operator fun invoke(recruitmentId: Long): Flow<Boolean> {
        return bookmarkRepository.observeBookmarkStatus(recruitmentId)
    }
}
