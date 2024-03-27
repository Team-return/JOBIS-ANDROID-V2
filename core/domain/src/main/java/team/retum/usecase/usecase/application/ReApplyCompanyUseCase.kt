package team.retum.usecase.usecase.application

import team.retum.common.enums.AttachmentType
import team.retum.data.repository.application.ApplicationRepository
import team.retum.network.model.request.application.ApplyCompanyRequest
import javax.inject.Inject

class ReApplyCompanyUseCase @Inject constructor(
    private val applicationRepository: ApplicationRepository,
) {
    suspend operator fun invoke(
        applicationId: Long,
        attachments: List<Pair<String, AttachmentType>>,
    ) = runCatching {
        applicationRepository.reApplyCompany(
            applicationId = applicationId,
            applyCompanyRequest = ApplyCompanyRequest(
                attachments = attachments.map {
                    ApplyCompanyRequest.Attachments(
                        url = it.first,
                        attachmentType = it.second,
                    )
                },
            ),
        )
    }
}
