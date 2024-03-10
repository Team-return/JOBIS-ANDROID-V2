package team.retum.usecase.usecase.notice

import team.retum.data.repository.notice.NoticeRepository
import team.retum.data.repository.notification.NotificationRepository
import team.retum.usecase.entity.notice.toNoticesEntity
import javax.inject.Inject

class FetchNoticesUseCase @Inject constructor(
    private val noticeRepository: NoticeRepository,
){
    suspend operator fun invoke(
        page: Int,
    ) = runCatching {
        noticeRepository.fetchNotices(page = page).toNoticesEntity()
    }
}
