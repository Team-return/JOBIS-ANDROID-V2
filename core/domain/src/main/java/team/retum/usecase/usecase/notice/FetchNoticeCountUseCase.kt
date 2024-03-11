package team.retum.usecase.usecase.notice

import team.retum.data.repository.company.CompanyRepository
import team.retum.data.repository.notice.NoticeRepository
import team.retum.usecase.entity.notification.toNoticeCountEntity
import javax.inject.Inject

class FetchNoticeCountUseCase @Inject constructor(
    private val noticeRepository: NoticeRepository,
) {
    suspend operator fun invoke(title: String?) = runCatching {
        noticeRepository.fetchPageCount(title = title).toNoticeCountEntity()
    }
}
