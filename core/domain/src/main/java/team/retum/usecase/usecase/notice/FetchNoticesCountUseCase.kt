package team.retum.usecase.usecase.notice

import team.retum.data.repository.notice.NoticeRepository
import team.retum.usecase.entity.notice.toEntity
import javax.inject.Inject

class FetchNoticesCountUseCase @Inject constructor(
    private val noticeRepository: NoticeRepository,
) {
    suspend operator fun invoke() = runCatching {
        noticeRepository.fetchNoticesCount().toEntity()
    }
}
