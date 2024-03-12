package team.retum.jobis.notice.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import team.retum.common.base.BaseViewModel
import team.retum.usecase.entity.notice.NoticeDetailsEntity
import team.retum.usecase.usecase.notice.FetchNoticeDetailsUseCase
import javax.inject.Inject

@HiltViewModel
internal class NoticeDetailsViewModel @Inject constructor(
    private val fetchNoticeDetailsUseCase: FetchNoticeDetailsUseCase,
) : BaseViewModel<NoticeDetailsState, NoticeDetailsSideEffect>(NoticeDetailsState.getDefaultState()) {

    internal fun fetchNoticeDetails(noticeId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchNoticeDetailsUseCase(noticeId = noticeId)
                .onSuccess {
                    setState { state.value.copy(noticeDetailsEntity = it) }
                }
                .onFailure {
                    postSideEffect(NoticeDetailsSideEffect.BadRequest)
                }
        }
    }
}

internal data class NoticeDetailsState(
    val noticeId: Long,
    val noticeDetailsEntity: NoticeDetailsEntity,
) {
    companion object {
        fun getDefaultState() = NoticeDetailsState(
            noticeId = 0,
            noticeDetailsEntity = NoticeDetailsEntity(
                title = "",
                content = "",
                createdAt = "",
                attachments = emptyList(),
            ),
        )
    }
}

internal sealed interface NoticeDetailsSideEffect {
    data object BadRequest : NoticeDetailsSideEffect
}
